package com.filipve1994.personalexercisespringbootcrudapi.errorhandling.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * So then the ApiValidationError is a class that extends ApiSubError
 * and expresses validation problems encountered during the REST call.
 */

//@Data
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
//    private String propertyPath;
    private String[] codes;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}