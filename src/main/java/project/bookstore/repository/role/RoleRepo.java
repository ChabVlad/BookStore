package project.bookstore.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Role;
import project.bookstore.model.RoleName;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByRole(RoleName name);
}
