package myCredit.repository;

import myCredit.domain.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Integer> {
    List<Credit> findByBank(String bank);
    List<Credit> findByTitle(String title);
    List<Credit> findByDateStart(LocalDate dateStart);
    List<Credit> findByDateEnd(LocalDate dateEnd);
}
