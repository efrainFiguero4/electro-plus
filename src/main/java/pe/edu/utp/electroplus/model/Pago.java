package pe.edu.utp.electroplus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaPago;

    private String direccion;

    private String nombreTarjeta;

    private String numeroTarjeta;

    @Transient
    private String dueDateYYMM;

    @Transient
    private String cvv;

    private BigDecimal montoTotal;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Pedido pedido;

    private String tipoDocumento;
    private String nroDocumento;

    @Builder.Default
    private String status = Carrito.ESTADO.FACTURADO.name();

    public String getFormatDate() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.fechaPago);
    }

    public String finaliza() {
        String[] strings = this.numeroTarjeta.split("-");
        return strings[strings.length - 1];
    }
}
