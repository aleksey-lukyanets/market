package market.dao;

import market.domain.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * ДАО единицы хранения. 
 */
public interface StorageDAO extends CrudRepository<Storage, Long>, JpaRepository<Storage, Long>
{
    public Page<Storage> findByAvailable(boolean available, Pageable pageable);
}
