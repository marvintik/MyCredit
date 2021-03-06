package myCredit.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserFacebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String idOauth;
    String name;
    String picture;
    String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
