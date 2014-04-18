package market.controller.backend;

import javax.validation.Valid;
import market.service.RegionService;
import market.domain.Region;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Контроллер управления регионами.
 */
@Controller
@RequestMapping("/admin/regions")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class RegionController {
    
    @Autowired private RegionService regionService;

    /**
     * Перечень регионов.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String regions(Model model) {
        model.addAttribute("regions", regionService.findAllOrderById());
        return "admin/regions";
    }
    
    //------------------------------------------------- Создание нового региона

    /**
     * Страница добавления.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public String newRegion(Model model) {
        model.addAttribute("region", new Region());
        return "admin/regions/new";
    }

    /**
     * Сохранение нового региона.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String postRegion(
            @Valid Region region,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/regions/new";
        }
        regionService.save(region);
        return "redirect:/admin/regions";
    }

    //-------------------------------------------------- Редактирование региона

    /**
     * Страница редактирования.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{regionId}/edit")
    public String editRegion(
            @PathVariable long regionId,
            Model model
    ) {
        model.addAttribute("region", regionService.findOne(regionId));
        return "admin/regions/edit";
    }

    /**
     * Изменение региона.
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{regionId}")
    public String putRegion(
            @PathVariable long regionId,
            @Valid Region region,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/regions/edit";
        }
        regionService.save(region);//!
        return "redirect:/admin/regions";
    }

    //-------------------------------------------------------- Удаление региона

    /**
     * Удаление региона.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{regionId}")
    public String deleteRegion(
            @PathVariable long regionId
    ) {
        Region region = regionService.findOne(regionId);
        regionService.delete(region);
        return "redirect:/admin/regions";
    }
}
