package io.github.gabriel01souza.quarkussocial.rest;


import io.github.gabriel01souza.quarkussocial.domain.model.User;
import io.github.gabriel01souza.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel01souza.quarkussocial.rest.dto.CreateUserRequest;
import io.github.gabriel01souza.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    private final UserRepository repository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository repository,
                        Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User(userRequest.getName(), userRequest.getAge());
        repository.persist(user);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user).build();
    }

    @GET
    @Transactional
    public Response getAllUsers() {
        PanacheQuery<User> query = repository.findAll();
        List<User> listUsers = query.list();

        return Response.ok(listUsers).build();
    }

    /**
     * Método para atualizar o user
     * Ao finalizar a transação ele persiste o objeto em memória
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizarUser(@PathParam("id") Long id, CreateUserRequest userRequest) {
        User user = repository.findById(id);

        if (Objects.nonNull(user)) {
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = repository.findById(id);

        if (Objects.nonNull(user)) {
            repository.delete(user);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
