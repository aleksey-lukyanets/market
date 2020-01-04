package market.controller.frontend;

import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.service.DistilleryService;
import market.service.ProductService;
import market.service.RegionService;
import market.sorting.ISorter;
import market.sorting.SortingValuesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер витрины. 
 */
@Controller
@RequestMapping("/regions")
public class ShowcaseController {
    private final RegionService regionService;
    private final ProductService productService;
    private final DistilleryService distilleryService;
    private final ISorter<Product> productSorting;

    public ShowcaseController(RegionService regionService, ProductService productService,
        DistilleryService distilleryService, ISorter<Product> productSorting)
    {
        this.regionService = regionService;
        this.productService = productService;
        this.distilleryService = distilleryService;
        this.productSorting = productSorting;
    }

    /**
     * Страница товаров региона. Фильтрация по винокурне и сортировка.
     *
     * @param regionId идентификатор региона
     * @param sortingValues параметры сортировки
     * @param distilleryId идентификатор винокурни
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{regionId}")
    public String getRegionProducts(
            @PathVariable long regionId,
            SortingValuesDTO sortingValues,
            @RequestParam(value = "dist", required = false, defaultValue = "0") Long distilleryId,
            Model model
    ) {
        Region region = regionService.findOne(regionId);

        PageRequest request = productSorting.updateSorting(sortingValues);
        Page<Product> pagedList;
        if (distilleryId == 0) {
            pagedList = productService.findByDistilleriesOfRegion(region, request);
        } else {
            Distillery distillery = distilleryService.findOne(distilleryId);
            pagedList = productService.findByDistillery(distillery, request);
            model.addAttribute("currentDistilleryTitle", distillery.getTitle());
        }
        productSorting.prepareModel(model, pagedList);
        model.addAttribute("distilleries", distilleryService.findByRegionOrderByTitle(region));

        model.addAttribute("selectedRegion", region);
        model.addAttribute("regions", regionService.findAllOrderByName());
        return "regions";
    }
}
