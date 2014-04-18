package market.controller.frontend;

import market.service.RegionService;
import market.domain.Region;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.ui.Model;

/**
 * Контроллер пользовательского интерфейса.
 */
@Controller
@EnableEntityLinks
public class FrontendController {

    @Autowired
    private RegionService regionService;

    /**
     * Главная страница.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("regions", regionService.findAllOrderByName());
        model.addAttribute("selectedRegion", Region.NULL);
        return "index";
    }
    
    /**
     * Страница входа в магазин.
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * Детали реализации.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/inside")
    public String whatsInside() {
        return "inside";
    }

    /**
     * Описание веб-службы REST.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rest-api")
    public String restApi() {
        return "rest";
    }
}
