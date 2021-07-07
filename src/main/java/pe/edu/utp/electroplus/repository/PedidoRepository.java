package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.electroplus.model.Pedido;

import java.util.List;
import java.util.Map;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    public List<Pedido> findByIdUsuario(Long idUsuario);

    @Query(value = "SELECT pr.producto.descripcion as descripcion, SUM(pr.cantidad) as cantidad, SUM(pr.precio*pr.cantidad) as montototal  FROM Pedido o JOIN o.productos pr GROUP BY pr.producto.descripcion ORDER BY cantidad DESC")
    List<Map<String, Object>> querySumaTotal();

}
