package com.example.articlewebapp.web.rest;


import com.example.articlewebapp.domain.Article;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.repository.ArticleRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.service.dto.ArticleDTO;
import com.example.articlewebapp.service.dto.mapper.ArticleMapper;
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

import static com.example.articlewebapp.web.rest.UserResourceIT.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ArticleResource} REST controller.
 *
 * @author Youssef Agagg
 */
@SpringBootTest
@AutoConfigureMockMvc
class ArticleResourceIT {

    static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    static final String DEFAULT_SUMMERY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMERY = "BBBBBBBBBB";

    static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";


    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleMapper articleMapper;


    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;
    
    private static User user;

   

   
  

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
        article.setAuthor(user);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();
        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticle.getSummary()).isEqualTo(DEFAULT_SUMMERY);
        assertThat(testArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticle.getViews()).isEqualTo(0l);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createArticleWithBySettingId() throws Exception {
        article.setId(1L);
        ArticleDTO articleDTO = articleMapper.toDto(article);

        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void createArticleWithInvalidData() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        article.setTitle(null);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {

        article.setDateCreated(Instant.now());
        articleRepository.saveAndFlush(article);


        restArticleMockMvc
            .perform(get(ENTITY_API_URL ))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMERY)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));

    }



    @Test
    @Transactional
    void getArticle() throws Exception {

        article.setDateCreated(Instant.now());
        article.setViews(0l);
        articleRepository.saveAndFlush(article);

        restArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMERY))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }



    @Test
    @Transactional
    void getNonExistingArticle() throws Exception {
        restArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateArticle() throws Exception {

        article.setDateCreated(Instant.now());
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();


        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle.setTitle(UPDATED_TITLE);
        updatedArticle.setContent(UPDATED_CONTENT);
        updatedArticle.setSummary(UPDATED_SUMMERY);
        ArticleDTO articleDTO = articleMapper.toDto(updatedArticle);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isOk());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getSummary()).isEqualTo(UPDATED_SUMMERY);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());


        ArticleDTO articleDTO = articleMapper.toDto(article);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isNotFound());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateWithIdMismatchArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());


        ArticleDTO articleDTO = articleMapper.toDto(article);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void updateWithMissingIdPathParamArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        ArticleDTO articleDTO = articleMapper.toDto(article);

        restArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isMethodNotAllowed());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    
    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_USERNAME)
    void deleteArticle() throws Exception {

        article.setDateCreated(Instant.now());
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        restArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, article.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
