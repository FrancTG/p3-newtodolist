package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import todolist.authentication.ManagerUserSession;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("about")
    public String about(Model model) {
        Long userId = managerUserSession.usuarioLogeado();
        UsuarioData user = null;
        boolean userIsLoggedIn = userId != null;
        if (userId != null) {
            user = usuarioService.findById(userId);
        }

        model.addAttribute("userIsLoggedIn", userIsLoggedIn);
        model.addAttribute("user",user);
        return "about";
    }

    @GetMapping("registered")
    public String registered(Model model) {
        model.addAttribute("users",usuarioService.getAllUsers());
        return "registered";
    }

    @GetMapping("/registered/{id}")
    public String registeredUserDetails(@PathVariable(value="id") Long idUsuario,Model model) {
        model.addAttribute("user",usuarioService.findById(idUsuario));
        return "registeredUserDetails";
    }
}
