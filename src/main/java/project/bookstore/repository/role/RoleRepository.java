package project.bookstore.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Role;
import project.bookstore.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleName name);
}
