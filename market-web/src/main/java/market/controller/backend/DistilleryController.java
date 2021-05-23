package market.controller.backend;

import market.domain.Distillery;
import market.dto.DistilleryDTO;
import market.dto.RegionDTO;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.RegionDtoAssembler;
import market.service.DistilleryService;
import market.service.RegionService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/admin/distilleries")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class DistilleryController {
	private static final String DISTILLERIES_BASE = "admin/distilleries";
	private static final String DISTILLERIES_NEW = DISTILLERIES_BASE + "/new";
	private static final String DISTILLERIES_EDIT = DISTILLERIES_BASE + "/edit";

	private final DistilleryService distilleryService;
	private final RegionService regionService;
	private final RegionDtoAssembler regionDtoAssembler = new RegionDtoAssembler();
	private final DistilleryDtoAssembler distilleryDtoAssembler = new DistilleryDtoAssembler();

	public DistilleryController(DistilleryService distilleryService, RegionService regionService) {
		this.distilleryService = distilleryService;
		this.regionService = regionService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allDistilleries(Model model) {
		List<DistilleryDTO> distilleriesDto = distilleryService.findAll().stream()
			.map(distilleryDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("distilleries", distilleriesDto);
		return DISTILLERIES_BASE;
	}

	//------------------------------------------------ Creating new distillery

	@RequestMapping(method = RequestMethod.GET, value = "/new")
	public String newDistillery(Model model) {
		List<RegionDTO> regionsDto = regionService.findAll().stream()
			.map(regionDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("regions", regionsDto);
		model.addAttribute("distillery", new Distillery());
		return DISTILLERIES_NEW;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/new")
	public String postDistillery(
		@Valid DistilleryDTO distilleryDto, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return DISTILLERIES_NEW;

		Distillery newDistillery = distilleryDtoAssembler.toDomain(distilleryDto);
		distilleryService.create(newDistillery, distilleryDto.getRegion());
		return "redirect:/" + DISTILLERIES_BASE;
	}

	//------------------------------------------------ Changing distillery

	@RequestMapping(method = RequestMethod.GET, value = "/{distilleryId}/edit")
	public String editDistillery(
		@PathVariable long distilleryId, Model model
	) {
		List<RegionDTO> regionsDto = regionService.findAll().stream()
			.map(regionDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("regions", regionsDto);

		Distillery distillery = distilleryService.findById(distilleryId);
		model.addAttribute("distillery", distilleryDtoAssembler.toModel(distillery));

		return DISTILLERIES_EDIT;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{distilleryId}/edit")
	public String putDistillery(
		@PathVariable long distilleryId,
		@Valid DistilleryDTO distilleryDto, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return DISTILLERIES_EDIT;

		Distillery changedDistillery = distilleryDtoAssembler.toDomain(distilleryDto);
		distilleryService.update(distilleryId, changedDistillery, distilleryDto.getRegion());
		return "redirect:/" + DISTILLERIES_BASE;
	}

	//------------------------------------------------------ Removing distillery

	@RequestMapping(method = RequestMethod.POST, value = "/{distilleryId}/delete")
	public String deleteDistillery(@PathVariable long distilleryId) {
		distilleryService.delete(distilleryId);
		return "redirect:/" + DISTILLERIES_BASE;
	}
}
