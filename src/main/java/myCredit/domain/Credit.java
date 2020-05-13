package myCredit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateEnd;
    Double cost;
    Double monthPay;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    Person person;

}
