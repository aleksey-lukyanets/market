package market.controller.backend;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
