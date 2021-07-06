package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByCodigo(String codigo);
}
