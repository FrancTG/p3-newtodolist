package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;
import todolist.service.UsuarioService;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    @GetMapping("/usuarios/{id}/equipos")
    public String listarEquipos(@PathVariable(value="id") Long idUsuario,Model model) {
        UsuarioData usuario = usuarioService.findById(idUsuario);
        List<EquipoData> equipos = equipoService.findAllOrdenadoPorNombre();
        model.addAttribute("equipos",equipos);
        model.addAttribute("usuario", usuario);
        return "listaEquipos";
    }
}
