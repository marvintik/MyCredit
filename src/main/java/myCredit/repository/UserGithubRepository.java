package myCredit.repository;

import myCredit.domain.User;
import myCredit.domain.UserGithub;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGithubRepository extends CrudRepository<UserGithub, Integer> {
}
