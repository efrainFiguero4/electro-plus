package pe.edu.utp.electroplus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.electroplus.service.LoginService;

@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("")
    public String inicio() {
        return "redirect:/inicio";
    }

    @GetMapping("/login")
    public String iniciarSesion(Model model, String error, String logout) {
        if (loginService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("message", "Email o Contraseña son incorrectos.");

        if (logout != null)
            model.addAttribute("message", "Se ha cerrado sesión satifactoriamente.");

        model.addAttribute("titulo", "INICIAR SESSION");
        return "login";
    }

}
