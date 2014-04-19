package market.service;

import java.util.List;
import market.domain.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Сервис единицы хранения.
 */
public interface StorageService {

    void save(Storage storage);
    
    void delete(Storage storage);

    Storage findOne(long productId);

    List<Storage> findAll();

    Page<Storage> findAll(PageRequest request);

    Page<Storage> findByAvailable(boolean available, Pageable pageable);

    Page<Storage> fetchFilteredAndPaged(String available, PageRequest request);
}
