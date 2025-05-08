package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import todolist.authentication.ManagerUserSession;
import todolist.controller.exception.UsuarioNoLogeadoException;
import todolist.dto.EquipoData;
import todolist.dto.TareaData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;
import todolist.service.EquipoServiceException;
import todolist.service.UsuarioService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @GetMapping("/usuarios/{id}/equipos/nuevo")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {


        UsuarioData usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "formNuevoEquipo";
    }

    @PostMapping("/usuarios/{id}/equipos/nuevo")
    public String nuevaTarea(@PathVariable(value="id") Long idUsuario, @ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {


        equipoService.crearEquipo(equipoData.getNombre());
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/usuarios/" + idUsuario + "/equipos";
    }

    @GetMapping("/usuarios/{idUser}/equipo/{idTeam}")
    public String verUsuariosEquipo(@PathVariable(value="idUser") Long idUsuario,
                                    @PathVariable(value="idTeam") Long idEquipo,
                                 @ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {
        List<UsuarioData> usuarios = new ArrayList<UsuarioData>();
        boolean userIsOnTeam = false;
        try {
            usuarios = equipoService.usuariosEquipo(idEquipo);

            for (EquipoData currEquipo : equipoService.equiposUsuario(idUsuario)) {
                if (currEquipo.getId().equals(idEquipo)) {
                    userIsOnTeam = true;
                    break;
                }
            }
        } catch (EquipoServiceException ex) {
            System.out.println("No existen usuarios en el equipo");
        }


        EquipoData equipo = equipoService.recuperarEquipo(idEquipo);
        model.addAttribute("idUsuario",idUsuario);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("equipo",equipo);
        model.addAttribute("userIsOnTeam",userIsOnTeam);
        return "listarUsuarioEquipo";
    }

    @GetMapping("/usuarios/{idUser}/equipos/{idTeam}/join")
    public String entrarEnElEquipo(@PathVariable(value="idUser") Long idUsuario,
                                 @PathVariable(value="idTeam") Long idEquipo,
                                 @ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {
        try {
            equipoService.añadirUsuarioAEquipo(idEquipo,idUsuario);
        } catch (EquipoServiceException ex) {
            System.out.println("El usuario ya pertenece al equipo");
        }

        return "redirect:/usuarios/" + idUsuario + "/equipo/" + idEquipo;
    }

    @GetMapping("/usuarios/{idUser}/equipos/{idTeam}/leave")
    public String salirDelEquipo(@PathVariable(value="idUser") Long idUsuario,
                                   @PathVariable(value="idTeam") Long idEquipo,
                                   @ModelAttribute EquipoData equipoData, Model model,
                                   HttpSession session) {
        try {
            equipoService.borrarUsuarioDelEquipo(idEquipo,idUsuario);
        } catch(EquipoServiceException ex) {
            System.out.println("El usuario no pertenece al equipo ");
        }

        return "redirect:/usuarios/" + idUsuario + "/equipo/"+idEquipo;
    }
    @GetMapping("/borrarequipo/{idTeam}")
    public String borrarEquipo(@PathVariable(value="idTeam") Long idEquipo) {

        equipoService.borrarEquipo(idEquipo);
        return "redirect:/login";
    }
}
