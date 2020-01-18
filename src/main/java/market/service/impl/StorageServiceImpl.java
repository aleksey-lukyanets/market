package market.service.impl;

import market.dao.StorageDAO;
import market.domain.Storage;
import market.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса единицы хранения.
 */
public class StorageServiceImpl implements StorageService {
	private final StorageDAO storageDAO;

	public StorageServiceImpl(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	@Override
	public Page<Storage> fetchFilteredAndPaged(String available, PageRequest request) {
		Page<Storage> pagedList;
		if (available.equals("all")) {
			pagedList = findAll(request);
		} else {
			boolean availability = Boolean.valueOf(available);
			pagedList = findByAvailable(availability, request);
		}
		return pagedList;
	}

	@Transactional
	@Override
	public void save(Storage storage) {
		storageDAO.save(storage);
	}

	@Transactional
	@Override
	public void delete(Storage storage) {
		if (storage != null) {
			storageDAO.delete(storage);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Storage findOne(long productId) {
		return storageDAO.findOne(productId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Storage> findAll() {
		return storageDAO.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Storage> findAll(PageRequest request) {
		return storageDAO.findAll(request);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Storage> findByAvailable(boolean available, Pageable pageable) {
		return storageDAO.findByAvailable(available, pageable);
	}
}
