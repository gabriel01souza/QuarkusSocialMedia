package io.github.gabriel01souza.quarkussocial.domain.repository;

import io.github.gabriel01souza.quarkussocial.domain.model.Post;
import io.github.gabriel01souza.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

    public List<Post> findByUserId(User user){
        return find("user_id", user.getId()).list();
    }

}
