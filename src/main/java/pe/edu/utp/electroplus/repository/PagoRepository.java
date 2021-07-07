package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Pago;
import pe.edu.utp.electroplus.model.Pedido;

import java.util.List;
import java.util.Map;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    public List<Pago> findByIdUsuario(Long idUsuario);

}
