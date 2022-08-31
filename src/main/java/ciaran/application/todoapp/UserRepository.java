package ciaran.application.todoapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM user WHERE name = ?1", nativeQuery = true)
    User findByName(String name);

    @Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
    User findByEmail(String email);
}
