package todolist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;
import todolist.service.UsuarioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipoPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void getEquiposDevuelveEquipo() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@umh");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        EquipoData equipo = equipoService.crearEquipo("Ejemplo 1");

        this.mockMvc.perform(get("/usuarios/"+usuario.getId()+"/equipos")).andExpect((ResultMatcher) content().string(containsString("Ejemplo 1")));
    }

    @Test
    public void nuevoEquipo() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user2@umh");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        this.mockMvc.perform(get("/usuarios/"+usuario.getId()+"/equipos/nuevo")).andExpect((ResultMatcher) content().string(containsString("Nuevo equipo")));
    }

    @Test
    public void usuarioEnEquipo() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user3@umh");
        usuario.setNombre("user3");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        EquipoData equipo = equipoService.crearEquipo("Ejemplo de equipo A");
        equipoService.añadirUsuarioAEquipo(equipo.getId(),usuario.getId());

        this.mockMvc.perform(get("/equipo/"+equipo.getId())).andExpect((ResultMatcher) content().string(containsString("Ejemplo de equipo A")));
    }
}
