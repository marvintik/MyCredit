package myCredit.repository;

import myCredit.domain.User;
import myCredit.domain.UserGoogle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGoogleRepository extends CrudRepository<UserGoogle, String> {
    UserGoogle findByIdOauth(String idOauth);
}
