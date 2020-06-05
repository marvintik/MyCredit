package myCredit.repository;

import myCredit.domain.User;
import myCredit.domain.UserFacebook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFacebookRepository extends CrudRepository<UserFacebook, Integer> {
}
