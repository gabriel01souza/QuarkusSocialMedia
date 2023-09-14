package io.github.gabriel01souza.quarkussocial.rest.dto.post;

import io.github.gabriel01souza.quarkussocial.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        return new PostResponse(post);
    }

    public PostResponse(Post post) {
        this.text = post.getText();
        this.dateTime = post.getDate();
    }

}
