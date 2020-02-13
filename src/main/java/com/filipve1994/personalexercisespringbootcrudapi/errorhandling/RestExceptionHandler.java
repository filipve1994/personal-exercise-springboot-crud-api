package com.filipve1994.personalexercisespringbootcrudapi.errorhandling;

import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions.UserNotFoundException;
import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.models.ApiError;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * https://www.toptal.com/java/spring-boot-rest-api-error-handling
 * <p>
 * ExceptionHandler is a Spring annotation that provides a mechanism to treat exceptions that are thrown
 * during execution of handlers (Controller operations).
 * <p>
 * This annotation, if used on methods of controller classes,
 * will serve as the entry point for handling exceptions thrown within this controller only.
 * <p>
 * Altogether, the most common way is to use @ExceptionHandler on methods of @ControllerAdvice classes
 * so that the exception handling will be applied globally or to a subset of controllers.
 * <p>
 * ControllerAdvice is an annotation introduced in Spring 3.2, and as the name suggests,
 * is “Advice” for multiple controllers.
 * <p>
 * It is used to enable a single ExceptionHandler to be applied to multiple controllers.
 * <p>
 * This way we can in just one place define how to treat such an exception
 * and this handler will be called when the exception is thrown from classes that are covered by this ControllerAdvice.
 * <p>
 * The subset of controllers affected can defined by using the following selectors on
 * ControllerAdvice: annotations(), basePackageClasses(), and basePackages(). If no selectors are provided,
 * then the ControllerAdvice is applied globally to all controllers.
 * <p>
 * So by using @ExceptionHandler and @ControllerAdvice,
 * we’ll be able to define a central point for treating exceptions and wrapping them up in an ApiError object
 * with better organization than the default Spring Boot error handling mechanism.
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     * <p>
     * Customize the response for HttpMediaTypeNotSupportedException.
     * <p>This method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        logger.info("handleHttpMediaTypeNotSupported using!");

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));

        //return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    }

    /**
     * Customize the response for MissingPathVariableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.2
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info("handleMissingPathVariable using!");

        String error = ex.getVariableName() + " pathParameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));

        //return super.handleMissingPathVariable(ex, headers, status, request);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     * <p>
     * Customize the response for MissingServletRequestParameterException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.info("handleMissingServletRequestParameter using!");

        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));

        //return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }


//    /**
//     * Customize the response for TypeMismatchException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     *
//     * @param ex      the exception
//     * @param headers the headers to be written to the response
//     * @param status  the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleTypeMismatch(ex, headers, status, request);
//    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     * <p>
     * Customize the response for HttpMessageNotReadableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info("handleHttpMessageNotReadable using!");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        logger.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
        //return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }


    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info("handleHttpMessageNotWritable using!");

        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
        //return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info("handleMethodArgumentNotValid using!");

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);

        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info("handleNoHandlerFoundException using!");

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);

        //return super.handleNoHandlerFoundException(ex, headers, status, request);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        logger.info("handleConstraintViolation using!");

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        logger.error(ex.toString());
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

//    @ExceptionHandler(value = {
//            IncorrectDateException.class
//    })
//    protected ResponseEntity<Object> handleRequestPathVariableIsInvalid(RuntimeException ex) {
//        logger.info("RestExceptionhandler - handleRequestPathVariableIsInvalid");
//
//        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY);
//        apiError.setMessage(ex.getMessage());
//        if (ex.getCause() != null) {
//            logger.debug("inside ex.getCause() != null check.");
//            apiError.setDebugMessage(ex.getCause().getMessage());
//        }
////        apiError.setDebugMessage(ex.getCause().getMessage());
//        return buildResponseEntity(apiError);
//    }

    /**
     * handleEntityNotFound.
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    @ExceptionHandler(value = {
            UserNotFoundException.class
    })
    protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex) {
        logger.info("RestExceptionhandler - handleEntityNotFound");

        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage("Exception class used: " + ex.getClass().getSimpleName());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     *
     * @param ex the javax.persistence.EntityNotFoundException
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {

        logger.info("handleEntityNotFound java version using!");

        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {

        logger.info("handleDataIntegrityViolation java version using!");

        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle SQLGrammarExceptions, inspects the cause for different DB mapping causes.
     *
     * @param ex the SQLGrammarException
     * @return the ApiError object
     */
    @ExceptionHandler(SQLGrammarException.class)
    protected ResponseEntity<Object> SQLGrammarExceptions(SQLGrammarException ex, WebRequest request) {

        logger.info("SQLGrammarExceptions java version using!");

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        logger.info(ex.getMessage()); // could not extract ResultSet
        logger.info(ex.getSQLException().getMessage()); // ORA-00904: "B"."DISTANCE_FROM": invalid identifier
        apiError.setMessage("SQL sent to the database was invalid. It could be due to a syntax error or an invalid object reference. " +
                "Possibilities are: DB column name of table doesn't exist.");
        apiError.setDebugMessage("" + ex.getSQLException().getMessage());
        return buildResponseEntity(apiError);
        //return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle ResourceAccessException, when an I/O error happens like an external service cannot be accessed for example
     *
     * @param ex the ResourceAccessException
     * @return the ApiError object
     */
    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<Object> springRestTemplateResourceAccessException(ResourceAccessException ex, WebRequest request) {

        logger.info("SpringRestTemplateResourceAccessException using!");

        ApiError apiError = new ApiError(NOT_FOUND, ex);
        logger.info(ex.getMessage());
        return buildResponseEntity(apiError);
        //return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {

        logger.info("handleMethodArgumentTypeMismatch java version using!");

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        //return new ResponseEntity<>(apiError, apiError.getStatus());
        return buildResponseEntity(apiError);
    }

    //

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
