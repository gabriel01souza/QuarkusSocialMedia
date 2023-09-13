package io.github.gabriel01souza.quarkussocial.rest.dto.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class ResponseError {

    public static int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String message;
    private Collection<FieldError> errors;

    public ResponseError(String message, Collection<FieldError> errors) {
        this.message = message;
        this.errors = errors;
    }

    public static <T> ResponseError createFromValidation(
            Set<ConstraintViolation<T>> violations
    ) {
        List<FieldError> errors = violations
                .stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
        String message = "Violation Error";

        return new ResponseError(message, errors);
    }

    public Response withStatusCode(int code) {
        return Response.status(code).entity(this).build();
    }
}
