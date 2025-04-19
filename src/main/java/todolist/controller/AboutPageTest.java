package todolist.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AboutPageTest {

    private MockMvc mockMvc;

    public void getAboutDevuelveNombreAplicacion() throws Exception {
        this.mockMvc.perform(get("/about")).andExpect((ResultMatcher) content().string(containsString("ToDoList")));
    }

}
