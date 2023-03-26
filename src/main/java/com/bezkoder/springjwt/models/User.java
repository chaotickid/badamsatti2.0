package com.bezkoder.springjwt.models;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Card> cardIdList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    private int points = 0;

    private String activityStatus = "INACTIVE";

    private int forgotPasswordCode;

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password =  password;
    }


    //helper methods card add remove
    public void addCard(Card card){
        this.getCardIdList().add(card);
        card.setUser(this);
    }

    public void removeCard(Card card){
        this.removeCard(card);
        card.setUser(null);
    }
}
