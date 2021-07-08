package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Carrito;
import pe.edu.utp.electroplus.model.Pago;
import pe.edu.utp.electroplus.model.Pedido;
import pe.edu.utp.electroplus.model.Producto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    public List<Pago> findByUsuarioId(Long idUsuario);

    @Query(value = "SELECT p FROM Pago p WHERE p.usuario.id=?1 And p.fechaPago>=?2 and p.fechaPago<=?3")
//    @Query(nativeQuery = true, value = "select * from t_pago where usuario_id = ?1 and fecha_pago >=?2 and  fecha_pago <=?3")
    List<Pago> findByUsuarioId(Long usuario, Timestamp desde, Timestamp hasta);

}
