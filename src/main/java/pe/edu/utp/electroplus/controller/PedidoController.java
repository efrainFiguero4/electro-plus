package pe.edu.utp.electroplus.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.utp.electroplus.model.Carrito;
import pe.edu.utp.electroplus.model.Pedido;
import pe.edu.utp.electroplus.model.Usuario;
import pe.edu.utp.electroplus.repository.ClienteRepository;
import pe.edu.utp.electroplus.repository.PagoRepository;
import pe.edu.utp.electroplus.repository.PedidoRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PedidoController {

    private static final String VIEW_INDEX = "pedido/index";
    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ClienteRepository clienteRepository;

    @GetMapping("/pedidos")
    public String index(Model model, Principal principal) {
        Usuario usuario = clienteRepository.findByUsername(principal.getName());
        List<Pedido> listItems = pedidoRepository.findByIdUsuario(usuario.getId());
        model.addAttribute("pedidos", listItems);
        return VIEW_INDEX;
    }

    @GetMapping("/pedido/view/{id}")
    public String createSubmitForm(@PathVariable("id") Long idPedido, Model model) {
        List<Carrito> listItems = pedidoRepository.findById(idPedido).get().getProductos();
        model.addAttribute("detalles", listItems);
        return "pedido/detalle";
    }

}
