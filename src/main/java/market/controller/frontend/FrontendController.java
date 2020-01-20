package market.controller.frontend;

import market.domain.Region;
import market.service.RegionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Контроллер пользовательского интерфейса.
 */
@Controller
public class FrontendController {
	private final RegionService regionService;

	public FrontendController(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Главная страница.
	 */
	@RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("regions", regionService.findAllOrderByName());
		model.addAttribute("selectedRegion", Region.NULL);
		return "index";
	}

	/**
	 * Страница входа в магазин.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	/**
	 * Описание реализации магазина.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/inside")
	public String whatsInside() {
		return "inside";
	}

	/**
	 * Описание веб-службы REST.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest-api")
	public String restApi() {
		return "rest";
	}
}
