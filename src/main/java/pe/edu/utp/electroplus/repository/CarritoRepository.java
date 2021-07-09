package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Carrito;
import pe.edu.utp.electroplus.model.Producto;
import pe.edu.utp.electroplus.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    public List<Carrito> findByIdUsuarioAndStatus(Long idUsuario, String status);

    @Query(value = "SELECT COUNT(1) FROM Carrito o WHERE o.idUsuario=?1 AND o.status='PENDIENTE'")
    public Integer obtenerCantidadCarritoXUsuarioId(Long idUsuario);
}
