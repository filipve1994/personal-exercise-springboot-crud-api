package com.filipve1994.personalexercisespringbootcrudapi.errorhandling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ApiError {

    private static final Logger logger = LoggerFactory.getLogger(ApiError.class);

    /**
     * The status property holds the operation call status.
     * It will be anything from 4xx to signalize client errors or 5xx to mean server errors.
     * A common scenario is a http code 400 that means a BAD_REQUEST, when the client,
     * for example, sends an improperly formatted field, like an invalid email address.
     */
    private HttpStatus status;

    private int statusCode;

    /**
     * The timestamp property holds the date-time instance of when the error happened.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Brussels")
    private LocalDateTime timestamp;

    /**
     * The message property holds a user-friendly message about the error.
     */
    private String message;

    /**
     * The debugMessage property holds a system message describing the error in more detail.
     */
    private String debugMessage;

    /**
     * The subErrors property holds an array of sub-errors that happened.
     * This is used for representing multiple errors in a single call.
     * An example would be validation errors in which multiple fields have failed the validation.
     * The ApiSubError class is used to encapsulate those.
     */
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
        this.statusCode = status.value();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message, String[] codes) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message, codes));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage(),
//                null
                fieldError.getCodes()
        );
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage(),
//                cv.getPropertyPath().toString()
                new String[]{cv.getPropertyPath().toString()}
        );
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

}
