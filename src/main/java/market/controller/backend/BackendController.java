package market.controller.backend;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/admin")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class BackendController {

	/**
	 * Backend title page.
	 */
	@RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
	public String index() {
		return "admin/index";
	}
}
