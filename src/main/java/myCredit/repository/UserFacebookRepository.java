package myCredit.repository;

import myCredit.domain.User;
import myCredit.domain.UserFacebook;
import myCredit.domain.UserGoogle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFacebookRepository extends CrudRepository<UserFacebook, String> {
    UserFacebook findByIdOauth(String idOauth);
}
