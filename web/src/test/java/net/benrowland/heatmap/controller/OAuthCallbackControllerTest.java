package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.service.OAuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OAuthCallbackController.class)
public class OAuthCallbackControllerTest {
    private static final String AUTHORISATION_CODE = "aaa111";
    public static final String USERNAME = "username";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @Test
    public void test() throws Exception {
        mockMvc.perform(
                get("/token_exchange").
                param("state", USERNAME).
                param("code", AUTHORISATION_CODE)).
                andExpect(status().is3xxRedirection());

        then(oAuthService).should().authorise(USERNAME, AUTHORISATION_CODE);
    }
}
