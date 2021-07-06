package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.utp.electroplus.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
