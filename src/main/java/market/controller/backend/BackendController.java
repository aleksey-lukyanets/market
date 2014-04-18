package market.controller.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;

/**
 * Контроллер панели управления.
 */
@Controller
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class BackendController {

    /**
     * Главная страница панели управления.
     */
    @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/index";
    }
}
