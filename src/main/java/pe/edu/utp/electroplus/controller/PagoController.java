package pe.edu.utp.electroplus.controller;

import lombok.AllArgsConstructor;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static pe.edu.utp.electroplus.model.Carrito.ESTADO.*;
import static pe.edu.utp.electroplus.utils.Constants.ERROR;
import static pe.edu.utp.electroplus.utils.Constants.MENSAJE;

@Controller
@AllArgsConstructor
public class PagoController {

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
            return "pago/pago";
        }
        BigDecimal montoTotal = listItems.stream().
                map(n -> n.getPrecio().multiply(new BigDecimal(n.getCantidad()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        Pago pago = new Pago();
        pago.setFechaPago(new Date());
        pago.setMontoTotal(montoTotal);
        pago.setIdUsuario(usuario.getId());
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
            pago.setIdPedido(pedido.getId());

            pagoRepository.save(pago);

            model.addAttribute(MODEL_VIEW, pago);
            redirect.addFlashAttribute(MENSAJE, "Se registro su pago y se genero su pedido nro " + pedido.getId());
        }
        return "redirect:/carrito";
    }
}
