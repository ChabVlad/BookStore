package project.bookstore.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bookstore.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Boolean existsUserByEmail(String email);
}
