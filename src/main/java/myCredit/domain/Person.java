package myCredit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    String image;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    List<Credit> credits;
}
