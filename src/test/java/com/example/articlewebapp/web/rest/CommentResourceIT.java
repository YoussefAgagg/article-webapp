package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.domain.Article;
import com.example.articlewebapp.domain.Comment;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.repository.ArticleRepository;
import com.example.articlewebapp.repository.CommentRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.dto.CommentDTO;
import com.example.articlewebapp.dto.mapper.CommentMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.articlewebapp.web.rest.ArticleResourceIT.*;
import static com.example.articlewebapp.web.rest.UserResourceIT.*;
import static com.example.articlewebapp.web.rest.UserResourceIT.DEFAULT_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommentResource} REST controller.
 *
 * @author Youssef Agagg
 */
@SpringBootTest
@AutoConfigureMockMvc
class CommentResourceIT {

    private static final String DEFAULT_COMMENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_TEXT = "BBBBBBBBBB";


    private static final String ENTITY_API_URL = "/api/{article_id}/comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentMockMvc;

    private Comment comment;

    private User user;

    private Article article;

    @BeforeEach
    public void initTest() {
        user = new User();
        user.setUsername(DEFAULT_USERNAME );
        user.setPassword("jskdjsi");
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setUsername(DEFAULT_USERNAME);
        user.setEmail(DEFAULT_EMAIL);
        user=userRepository.saveAndFlush(user);

        article = new Article();
        article.setTitle(DEFAULT_TITLE);
        article.setSummary(DEFAULT_SUMMERY);
        article.setContent(DEFAULT_CONTENT);
        article.setViews(10l);
        article.setDateCreated(Instant.now());
        article.setAuthor(user);

        article=articleRepository.saveAndFlush(article);
        comment=new Comment();
        comment.setText(DEFAULT_COMMENT_TEXT);
        comment.setUser(user);
        comment.setArticle(article);
        comment.setDateCreated(Instant.now());
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();
        CommentDTO commentDTO = commentMapper.toDto(comment);
        restCommentMockMvc
            .perform(post(ENTITY_API_URL,article.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isCreated());


        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(DEFAULT_COMMENT_TEXT);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createCommentWithExistingId() throws Exception {

        comment.setId(1L);
        CommentDTO commentDTO = commentMapper.toDto(comment);

        int databaseSizeBeforeCreate = commentRepository.findAll().size();


        restCommentMockMvc
            .perform(post(ENTITY_API_URL,article.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createCommentwithInvalidData() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();

        comment.setText(null);

        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(post(ENTITY_API_URL,article.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComments() throws Exception {

        commentRepository.saveAndFlush(comment);


        restCommentMockMvc
            .perform(get(ENTITY_API_URL ,article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_COMMENT_TEXT)));
    }



    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateComment() throws Exception {

        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();


        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        // Disconnect from session so that the updates on updatedComment are not directly saved in db
        em.detach(updatedComment);
        updatedComment.setText(UPDATED_COMMENT_TEXT);
        CommentDTO commentDTO = commentMapper.toDto(updatedComment);

        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID,article.getId(), commentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isOk());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(UPDATED_COMMENT_TEXT);
        assertThat(testComment.getLastEdited()).isNotNull();
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID,article.getId(), commentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isNotFound());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateWithIdMismatchComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());

        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID,article.getId(), count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateWithMissingIdPathParamComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        comment.setId(count.incrementAndGet());


        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc
            .perform(put(ENTITY_API_URL,article.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isMethodNotAllowed());


        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }


    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void deleteComment() throws Exception {
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeDelete = commentRepository.findAll().size();

        restCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID,article.getId(), comment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
