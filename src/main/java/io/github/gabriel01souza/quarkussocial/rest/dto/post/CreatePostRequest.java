package io.github.gabriel01souza.quarkussocial.rest.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotBlank
    private String text;

}
