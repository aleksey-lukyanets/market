package market.controller.backend;

import market.domain.Distillery;
import market.domain.Region;
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

/**
 * Контроллер управления винокурнями.
 */
@Controller
@RequestMapping("/admin/distilleries")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class DistilleryController {
	private final DistilleryService distilleryService;
	private final RegionService regionService;

	public DistilleryController(DistilleryService distilleryService, RegionService regionService) {
		this.distilleryService = distilleryService;
		this.regionService = regionService;
	}

	/**
	 * Перечень винокурен.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String distillery(Model model) {
		model.addAttribute("distilleries", distilleryService.findAllOrderByTitle());
		return "admin/distilleries";
	}

	//------------------------------------------------ Создание новой категории

	/**
	 * Страница добавления.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/new")
	public String newDistillery(Model model) {
		model.addAttribute("distillery", new Distillery());
		model.addAttribute("regions", regionService.findAllOrderByName());
		return "admin/distilleries/new";
	}

	/**
	 * Сохранение новой винокурни.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String postDistillery(
		@Valid Distillery distillery,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return "admin/distilleries/new";
		}
		Region region = regionService.findOne(distillery.getRegion().getId());
		distillery.setRegion(region);
		distilleryService.save(distillery);
		return "redirect:/admin/distilleries";
	}

	//------------------------------------------------ Редактирование категории

	/**
	 * Страница редактирования.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{distilleryId}/edit")
	public String editDistillery(
		@PathVariable long distilleryId,
		Model model
	) {
		model.addAttribute("distillery", distilleryService.findOne(distilleryId));
		model.addAttribute("regions", regionService.findAllOrderByName());
		return "admin/distilleries/edit";
	}

	/**
	 * Изменение винокурни.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{distilleryId}")
	public String putDistillery(
		@PathVariable long distilleryId,
		@Valid Distillery distillery,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return "admin/distilleries/edit";
		}
		Region region = regionService.findOne(distillery.getRegion().getId());
		distillery.setRegion(region);
		distilleryService.save(distillery);//!
		return "redirect:/admin/distilleries";
	}

	//------------------------------------------------------ Удаление категории

	/**
	 * Удаление винокурни.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{distilleryId}")
	public String deleteDistillery(
		@PathVariable long distilleryId
	) {
		Distillery distillery = distilleryService.findOne(distilleryId);
		distilleryService.delete(distillery);
		return "redirect:/admin/distilleries";
	}
}
