package pe.edu.utp.electroplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.utp.electroplus.model.Carrito;

import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    public List<Carrito> findByIdUsuario(Long idUsuario);
}
