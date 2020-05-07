package myCredit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String bank;
    String title;
    LocalDate dateStart;
    LocalDate dateEnd;
    Double cost;
    Double monthPay;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    Person person;
}
