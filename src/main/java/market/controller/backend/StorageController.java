package market.controller.backend;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import market.domain.Storage;
import market.service.StorageService;
import market.sorting.ISorter;
import market.sorting.SortingValuesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер управления единицами хранения.
 */
@Controller
@RequestMapping("/admin/storage")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class StorageController {

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private ISorter<Storage> storageSorting;

    /**
     * Перечень единиц хранения.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getStorageUnits(
            SortingValuesDTO sortingValues,
            @RequestParam(value = "available", required = false, defaultValue = "all") String available,
            Model model
    ) {
        PageRequest request = storageSorting.updateSorting(sortingValues);
        Page<Storage> pagedList = storageService.fetchFilteredAndPaged(available, request);
        storageSorting.prepareModel(model, pagedList);
        
        model.addAttribute("currentAvailable", available);
        return "admin/storage";
    }

    /**
     * Установка наличия перечня товаров.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String postStorage(
            @RequestParam(value = "productIds", required = false) Long[] productIds,
            @RequestParam(value = "actualIds", required = false) Long[] actualIds
    ) {
        if ((actualIds == null) && (productIds == null)) {
            for (Storage stored : storageService.findAll()) {
                stored.setAvailable(false);
                storageService.save(stored);
            }
        } else {
            Set<Long> products = new HashSet<>(Arrays.asList(productIds));
            Set<Long> actuals = new HashSet<>(Arrays.asList(actualIds));
            for (Long actualId : products) {
                Storage stored = storageService.findOne(actualId);
                boolean refreshedAvailable = actuals.contains(stored.getId());
                stored.setAvailable(refreshedAvailable);
                storageService.save(stored);
            }
        }
        return "redirect:/admin/storage";
    }
}
