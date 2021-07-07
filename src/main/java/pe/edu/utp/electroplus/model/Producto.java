package pe.edu.utp.electroplus.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "t_producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String nombre;

    @NotNull
    private BigDecimal precio;

    @NotNull
    private BigDecimal descuento;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imagen;

    @NotNull
    private String nombreimagen;

    @NotNull
    private Integer categoria;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Transient
    private BigDecimal precioDescuento;

    @Transient
    private BigDecimal precioCarrito;

}
