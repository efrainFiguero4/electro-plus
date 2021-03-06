package pe.edu.utp.electroplus.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.electroplus.model.Carrito;
import pe.edu.utp.electroplus.model.Producto;
import pe.edu.utp.electroplus.model.Usuario;
import pe.edu.utp.electroplus.repository.CarritoRepository;
import pe.edu.utp.electroplus.repository.ProductoRepository;
import pe.edu.utp.electroplus.service.ClienteService;

import static pe.edu.utp.electroplus.utils.Constants.MENSAJE;

@Log4j2
@Controller
@AllArgsConstructor
public class CarritoController {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteService clienteService;

    @GetMapping("/carrito")
    public String carrito(Model model, Principal principal) {
        Usuario user = clienteService.findByUsername(principal.getName());
        List<Carrito> carritos = carritoRepository.findByIdUsuarioAndStatus(user.getId(), Carrito.ESTADO.PENDIENTE.name());
        BigDecimal sumPrecio = carritos.stream().map(Carrito::getPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("carritos", carritos);
        model.addAttribute("total", sumPrecio);

        return "carrito/create";
    }

    @GetMapping("/carrito/agregar/{idProducto}")
    public String carritoAgregar(@PathVariable Long idProducto, Principal principal, RedirectAttributes redirect) {
        Usuario user = clienteService.findByUsername(principal.getName());
        Producto producto = productoRepository.getOne(idProducto);
        carritoRepository.saveAndFlush(Carrito.builder()
                .cantidad(1)
                .precio(producto.getPrecio().multiply(new BigDecimal(1)))
                .producto(producto)
                .idUsuario(user.getId())
                .build());
        redirect.addFlashAttribute(MENSAJE, "Producto agregado correcamente");

        return "redirect:/carrito";
    }

    @GetMapping("/carrito/eliminar/{idcarrito}")
    public String carritoEliminar(@PathVariable Long idcarrito, RedirectAttributes redirect) {
        carritoRepository.deleteById(idcarrito);
        redirect.addFlashAttribute(MENSAJE, "EL producto se ha quitado del carrito de compras.");
        return "redirect:/carrito";
    }

    @GetMapping(value = "/carrito/update/{idcarrito}/{cantidad}")
    public ResponseEntity<String> actualizarCantidad(@PathVariable Long idcarrito, @PathVariable Integer cantidad) {
        log.info("carrito: {} ,cantidad: {} ", idcarrito, cantidad);
        if (cantidad > 0) {
            Carrito car = carritoRepository.getOne(idcarrito);
            car.setCantidad(cantidad);
            car.setPrecio(car.getProducto().getPrecio().multiply(new BigDecimal(cantidad)));
            carritoRepository.save(car);
        }
        return ResponseEntity.ok("OK");
    }
}
