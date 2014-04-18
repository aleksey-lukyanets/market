package market.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpSession;
import market.data.UserData;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 */
public class TestUtil {
    
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static HttpSession loginAndReturnSession(MockMvc mockMvc, String login, String password) throws Exception {
        HttpSession session = mockMvc.perform(post("/j_spring_security_check")
                .param("email", login)
                .param("password", password)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
        Assert.assertNotNull(session);
        return session;
    }

    public static void logout(MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/j_spring_security_logout"))
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"));
    }
}
