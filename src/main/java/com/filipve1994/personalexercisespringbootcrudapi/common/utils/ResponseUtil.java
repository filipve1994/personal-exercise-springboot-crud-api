package com.filipve1994.personalexercisespringbootcrudapi.common.utils;

import java.net.URI;
import javax.json.Json;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
public class ResponseUtil {

    private static final int OK_STATUS_CODE = HttpStatus.OK.value();
    private static final int BAD_REQUEST_STATUS_CODE = HttpStatus.BAD_REQUEST.value();
    private static final int CREATED_STATUS_CODE = HttpStatus.CREATED.value();
    private static final int CONFLICT_STATUS_CODE = HttpStatus.CONFLICT.value();
    private static final int NOT_FOUND_STATUS_CODE = HttpStatus.NOT_FOUND.value();

    public static ResponseEntity okResponse() {
        String successJson = Json.createObjectBuilder()
                .add("statusCode", OK_STATUS_CODE)
                .add("successMessage", "Success")
                .build().toString();

        return okResponse(successJson);
    }

    public static ResponseEntity okResponse(String responseMessage) {
        return ResponseEntity.ok().body(responseMessage);
    }

    public static ResponseEntity okResponse(Object responseBody) {
        return ResponseEntity.ok().body(responseBody);
    }

    public static ResponseEntity createdResponse(Object responseBody) {
        return ResponseEntity.created(URI.create(StringUtils.EMPTY)).body(responseBody);
    }

    public static ResponseEntity createdResponse() {
        return ResponseEntity.created(URI.create(StringUtils.EMPTY)).body(null);
    }

    public static ResponseEntity acceptedResponse() {
        return ResponseEntity.accepted().body(null);
    }


    public static ResponseEntity badResponse(String responseMessage) {
        return ResponseEntity.badRequest()
                .body(new OperationResult(BAD_REQUEST_STATUS_CODE, responseMessage));
    }

    public static ResponseEntity badResponse(String responseMessage, Object object) {
        return ResponseEntity.badRequest().
                body(new OperationResult(BAD_REQUEST_STATUS_CODE, responseMessage, object));
    }

    public static ResponseEntity conflictResponse(String responseMessage) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new OperationResult(CONFLICT_STATUS_CODE, responseMessage));
    }

    public static ResponseEntity noContentResponse() {
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity notFoundResponse(String responseMessage) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new OperationResult(NOT_FOUND_STATUS_CODE, responseMessage));
    }

    public static ResponseEntity notFoundResponse(String responseMessage, Object object) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new OperationResult(NOT_FOUND_STATUS_CODE, responseMessage, object));
    }

    @EqualsAndHashCode
    @Getter
    @Setter
    @NoArgsConstructor
    public static class OperationResult {

        private int errorCode;
        private String errorMessage;
        private Object ResponseObject;

        OperationResult(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        OperationResult(int errorCode, String errorMessage, Object ResponseObject) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.ResponseObject = ResponseObject;
        }

    }

}
