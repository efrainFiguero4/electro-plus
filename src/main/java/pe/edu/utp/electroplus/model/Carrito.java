package pe.edu.utp.electroplus.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_carrito")
public class Carrito {
    public enum ESTADO {PENDIENTE, PAGADO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Producto producto;

    @NotNull
    private Long idUsuario;

    @NotNull
    private Integer cantidad;

    private BigDecimal precio;

    @Builder.Default
    private String status = ESTADO.PENDIENTE.name();
}
