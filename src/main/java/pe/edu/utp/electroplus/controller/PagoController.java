package pe.edu.utp.electroplus.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.electroplus.model.*;
import pe.edu.utp.electroplus.repository.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static pe.edu.utp.electroplus.model.Carrito.ESTADO.*;
import static pe.edu.utp.electroplus.utils.Constants.ERROR;
import static pe.edu.utp.electroplus.utils.Constants.MENSAJE;

@Log4j2
@Controller
@AllArgsConstructor
public class PagoController {

    private static final String PEDIDO_INDEX = "pedido/index";
    private static final String MODEL_VIEW = "pago";
    private final CarritoRepository carritoRepository;
    private final ClienteRepository clienteRepository;
    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    @GetMapping("/pago")
    public String index(Model model, Principal principal, RedirectAttributes redirect) {
        Usuario usuario = clienteRepository.findByUsername(principal.getName());
        List<Carrito> listItems = carritoRepository.findByIdUsuarioAndStatus(usuario.getId(), PENDIENTE.name());
        if (listItems.isEmpty()) {
            redirect.addFlashAttribute(MENSAJE, "Carrito Vacio");
            return "redirect:/carrito";
        }
        BigDecimal montoTotal = listItems.stream().map(Carrito::getPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
        Pago pago = new Pago();
        pago.setFechaPago(new Date());
        pago.setMontoTotal(montoTotal);
        pago.setUsuario(usuario);
        pago.setNombreTarjeta(usuario.getNombre().concat(" ").concat(usuario.getApellidos()));
        model.addAttribute(MODEL_VIEW, pago);
        return "pago/pago";
    }

    @PostMapping("/pago/create")
    public String createSubmitForm(Model model, @Valid Pago pago, Principal principal,
                                   BindingResult result, RedirectAttributes redirect) {
        Usuario usuario = clienteRepository.findByUsername(principal.getName());
        if (result.hasFieldErrors()) {
            redirect.addFlashAttribute(ERROR, "Debe completar los campos requeridos");
        } else {
            List<Carrito> listaCarrito = carritoRepository.findByIdUsuarioAndStatus(usuario.getId(), PENDIENTE.name());
            listaCarrito.forEach(o -> o.setStatus(PAGADO.name()));

            Pedido pedido = pedidoRepository.saveAndFlush(Pedido.builder()
                    .idUsuario(usuario.getId())
                    .fechaPedido(new Date())
                    .productos(listaCarrito)
                    .montoTotal(pago.getMontoTotal())
                    .build());

            pago.setFechaPago(new Date());
            pago.setPedido(pedido);

            pagoRepository.save(pago);

            model.addAttribute(MODEL_VIEW, pago);
            redirect.addFlashAttribute(MENSAJE, "Se registro su pago y se genero su pedido nro " + pedido.getId());
        }
        return "redirect:/carrito";
    }

    @GetMapping("/pedidos")
    public String index(Model model, Principal principal, Filtro filtro) throws ParseException {
        log.info("fecha: {}", filtro.getFecha());
        Usuario usuario = clienteRepository.findByUsername(principal.getName());
        List<Pago> pagos = Strings.isBlank(filtro.fecha)
                ? pagoRepository.findByUsuarioId(usuario.getId())
                : pagoRepository.findByUsuarioId(usuario.getId(), filtro.desde(), filtro.hasta());
        model.addAttribute("pagos", pagos);
        model.addAttribute("filtro", filtro);
        return PEDIDO_INDEX;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Filtro {
        private String fecha;

        public Timestamp desde() throws ParseException {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return new Timestamp(format.parse(this.fecha).getTime());
        }

        public Timestamp hasta() throws ParseException {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = format.parse(this.fecha);
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            return new Timestamp(c.getTime().getTime());
        }
    }
}
