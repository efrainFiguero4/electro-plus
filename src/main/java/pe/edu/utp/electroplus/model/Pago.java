package pe.edu.utp.electroplus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaPago;

    private String nombreTarjeta;

    private String numeroTarjeta;

    @Transient
    private String dueDateYYMM;

    @Transient
    private String cvv;

    private BigDecimal montoTotal;

    @NotNull
    private Long idUsuario;

    private Long idPedido;

    private String tipoDocumento;
    private String nroDocumento;
}
