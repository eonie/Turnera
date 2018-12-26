package org.turnera.server.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tur_user")
public class User {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public interface ListView {};
    @Id
    @GeneratedValue
    @JsonView(ListView.class)
    private Long id;
    //@JsonView(ListView.class)
    private String username;
    private String name;
    private String email;
}
