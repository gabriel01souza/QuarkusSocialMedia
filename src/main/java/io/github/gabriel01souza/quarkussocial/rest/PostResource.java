package io.github.gabriel01souza.quarkussocial.rest;

import io.github.gabriel01souza.quarkussocial.domain.model.Post;
import io.github.gabriel01souza.quarkussocial.domain.model.User;
import io.github.gabriel01souza.quarkussocial.domain.repository.PostRepository;
import io.github.gabriel01souza.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel01souza.quarkussocial.rest.dto.post.CreatePostRequest;
import io.github.gabriel01souza.quarkussocial.rest.dto.post.PostResponse;
import io.github.gabriel01souza.quarkussocial.rest.dto.validation.ResponseError;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private final Validator validator;

    @Inject
    public PostResource(
            PostRepository postRepository,
            Validator validator,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);
        if (Objects.isNull(user)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(postRequest);
        if (!violations.isEmpty()) {
            ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        postRepository.persist(
                new Post(postRequest.getText(), user)
        );

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }

    @GET
    @Transactional
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        if (Objects.isNull(user)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Post> posts = postRepository.find("user", Sort.by("date", Sort.Direction.Descending), user).list();

        var response = posts.stream().map(PostResponse::fromEntity).toList();

        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

}
