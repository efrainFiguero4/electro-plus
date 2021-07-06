package pe.edu.utp.electroplus.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer>{

}
