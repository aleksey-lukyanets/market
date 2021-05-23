package market.controller.frontend;

import market.domain.Region;
import market.dto.RegionDTO;
import market.dto.assembler.RegionDtoAssembler;
import market.service.RegionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Frontend root pages controller.
 */
@Controller
public class FrontendController {
	private final RegionService regionService;
	private final RegionDtoAssembler regionDTOAssembler = new RegionDtoAssembler();

	public FrontendController(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Title page.
	 */
	@RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		List<RegionDTO> regionsDto = regionService.findAll().stream()
			.map(regionDTOAssembler::toModel)
			.collect(toList());
		model.addAttribute("regions", regionsDto);
		model.addAttribute("selectedRegion", Region.NULL);
		return "index";
	}

	/**
	 * Login page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	/**
	 * Implementation description page.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/inside")
	public String whatsInside() {
		return "inside";
	}

	/**
	 * REST description page.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest-api")
	public String restApi() {
		return "rest";
	}
}
