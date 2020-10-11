package myCredit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.Nullable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;

    @Column(nullable = true)
    String image;

    @Column(nullable = true)
    byte[] imageFile;

    @Column
    String tokenMono;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    List<Credit> credits;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "users_id")
    User user;
}
