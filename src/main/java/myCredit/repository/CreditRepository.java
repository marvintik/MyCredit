package myCredit.repository;

import myCredit.domain.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Integer> {
}
