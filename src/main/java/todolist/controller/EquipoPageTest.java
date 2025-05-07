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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        this.mockMvc.perform(get("/usuarios/"+usuario.getId()+"/equipo/"+equipo.getId())).andExpect((ResultMatcher) content().string(containsString("Ejemplo de equipo A")));
    }

    @Test
    public void usuarioEntraEquipo() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user3@umh");
        usuario.setNombre("user3");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        UsuarioData usuario2 = new UsuarioData();
        usuario2.setEmail("user4@umh");
        usuario2.setNombre("user4");
        usuario2.setPassword("1234");
        usuario2 = usuarioService.registrar(usuario2);

        EquipoData equipo = equipoService.crearEquipo("Ejemplo de equipo A");

        // Entra el primer usuario al equipo
        this.mockMvc.perform(get("/usuarios/"+usuario.getId()+"/equipos/"+equipo.getId()+"/join"))
                .andExpect(status().is3xxRedirection());
        // Entra el segundo usuario
        this.mockMvc.perform(get("/usuarios/"+usuario2.getId()+"/equipos/"+equipo.getId()+"/join"))
                .andExpect(status().is3xxRedirection());
        // Comprueba si existe en la página
        this.mockMvc.perform(get("/usuarios/" + usuario.getId() + "/equipo/" + equipo.getId()))
                .andExpect(content().string(containsString("user3")));

        // Sale el primero usuario del equipo
        this.mockMvc.perform(get("/usuarios/"+usuario.getId()+"/equipos/"+equipo.getId()+"/leave"))
                .andExpect(status().is3xxRedirection());

        // Comprueba si todavia está el segundo usuario en el equipo
        this.mockMvc.perform(get("/usuarios/" + usuario.getId() + "/equipo/" + equipo.getId()))
                .andExpect(content().string(containsString("user4")));
    }
}
