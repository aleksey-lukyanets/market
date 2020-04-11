package market.controller.backend;

import market.domain.Region;
import market.dto.RegionDTO;
import market.dto.assembler.RegionDtoAssembler;
import market.service.RegionService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/admin/regions")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class RegionController {
	private static final String REGIONS_BASE = "admin/regions";
	private static final String REGIONS_NEW = REGIONS_BASE + "/new";
	private static final String REGIONS_EDIT = REGIONS_BASE + "/edit";

	private final RegionService regionService;
	private final RegionDtoAssembler regionDTOAssembler = new RegionDtoAssembler();

	public RegionController(RegionService regionService) {
		this.regionService = regionService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allRegions(Model model) {
		List<RegionDTO> regionsDto = regionService.findAll().stream()
			.sorted(Comparator.comparing(Region::getId))
			.map(regionDTOAssembler::toModel)
			.collect(toList());
		model.addAttribute("regions", regionsDto);
		return REGIONS_BASE;
	}

	//------------------------------------------------- Creating new region

	@RequestMapping(method = RequestMethod.GET, value = "/new")
	public String newRegion(Model model) {
		model.addAttribute("region", regionDTOAssembler.toModel(new Region()));
		return REGIONS_NEW;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/new")
	public String postRegion(
		@Valid RegionDTO regionDto, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return REGIONS_NEW;

		Region newRegion = regionDTOAssembler.toDomain(regionDto);
		regionService.create(newRegion);
		return "redirect:/" + REGIONS_BASE;
	}

	//-------------------------------------------------- Updating region

	@RequestMapping(method = RequestMethod.GET, value = "/{regionId}/edit")
	public String editRegion(
		@PathVariable long regionId, Model model
	) {
		Region region = regionService.findOne(regionId);
		model.addAttribute("region", regionDTOAssembler.toModel(region));
		return REGIONS_EDIT;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{regionId}/edit")
	public String putRegion(
		@PathVariable long regionId,
		@Valid RegionDTO regionDto, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return REGIONS_EDIT;

		Region changedRegion = regionDTOAssembler.toDomain(regionDto);
		regionService.update(regionId, changedRegion);
		return "redirect:/" + REGIONS_BASE;
	}

	//-------------------------------------------------------- Deleting region

	@RequestMapping(method = RequestMethod.POST, value = "/{regionId}/delete")
	public String deleteRegion(@PathVariable long regionId) {
		regionService.delete(regionId);
		return "redirect:/" + REGIONS_BASE;
	}
}
