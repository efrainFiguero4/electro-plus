package pe.edu.utp.electroplus.controller;

import static pe.edu.utp.electroplus.utils.Constants.CATEGORIAS;
import static pe.edu.utp.electroplus.utils.Constants.MENSAJE;
import static pe.edu.utp.electroplus.utils.Constants.PRODUCTO;
import static pe.edu.utp.electroplus.utils.Constants.TITULO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.utp.electroplus.model.Producto;
import pe.edu.utp.electroplus.repository.CategoriaRepository;
import pe.edu.utp.electroplus.repository.ProductoRepository;

@Controller
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping({ "/", "/inicio" })
    public String index(Model model) {
        model.addAttribute("productos", listarProductos());
        return "catalogo/catalogo";
    }

    @GetMapping("/productos")
    public String navigateActualizar(Model model) {
        model.addAttribute("productos", listarProductos());
        return "catalogo/listar";
    }

    @GetMapping("/producto")
    public String navigate(Model model) {
        Producto producto = new Producto();
        producto.setImagen("image/image.svg");
        model.addAttribute(PRODUCTO, producto);
        model.addAttribute(TITULO, "REGISTRAR PRODUCTO");
        model.addAttribute(CATEGORIAS, categoriaRepository.findAll());
        return "catalogo/formulario";
    }

    @GetMapping("/producto/{id}")
    public String navigateActualizar(Model model, @PathVariable Long id) {
        Producto producto = productoRepository.getOne(id);
        model.addAttribute(PRODUCTO, producto);
        model.addAttribute(TITULO, "ACTUALIZAR PRODUCTO");
        model.addAttribute(CATEGORIAS, categoriaRepository.findAll());
        return "catalogo/formulario";
    }

    @PostMapping("/producto/registrar")
    public String registrarProducto(@Valid Producto producto, BindingResult result, RedirectAttributes redirect) {
        Long productoId = producto.getId();
        if (result.hasFieldErrors()) {
            redirect.addFlashAttribute(PRODUCTO, producto);
            redirect.addFlashAttribute(MENSAJE, "Debe completar los campos requeridos");
        } else {
            productoRepository.saveAndFlush(producto);
            redirect.addFlashAttribute(MENSAJE,
                    "Se " + (Objects.nonNull(productoId) ? "actualizó el" : "registró un nuevo") + " producto");
        }
        if (Objects.nonNull(productoId))
            return "redirect:/producto/" + productoId;
        return "redirect:/producto";
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll().stream()
                .peek(p -> p.setPrecioDescuento(p.getDescuento().add(p.getPrecio()))).collect(Collectors.toList());
    }
}
