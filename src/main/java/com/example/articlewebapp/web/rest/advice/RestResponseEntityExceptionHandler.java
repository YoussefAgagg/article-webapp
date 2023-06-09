package com.example.articlewebapp.web.rest.advice;

import com.example.articlewebapp.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A global exception handler for REST API.
 *
 * @author Youssef Agagg
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT,request);
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<Object> handleAuthenticationException(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<Object> handleInvalidPasswordException(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  @ExceptionHandler(UsernameAlreadyUsedException.class)
  public ResponseEntity<Object> handleUsernameAlreadyUsedException(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  @ExceptionHandler(EmailAlreadyUsedException.class)
  public ResponseEntity<Object> handleEmailAlreadyUsedExceptionn(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> defaultExceptionHandler(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);

  }
  @ExceptionHandler(ChangeUsernameException.class)
  public ResponseEntity<Object> changeUserNameException(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(ResourceDoesntExistException.class)
  public ResponseEntity<Object> accountDoesntExistException(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> accessDeniedException(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> badRequestExceptionHandler(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
