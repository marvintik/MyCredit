package myCredit.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserGithub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String login;
    String avatar_url;
    String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
