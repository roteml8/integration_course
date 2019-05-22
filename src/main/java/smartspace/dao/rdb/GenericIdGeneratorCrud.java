package smartspace.dao.rdb;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GenericIdGeneratorCrud
	extends PagingAndSortingRepository<GenericIdGenerator, Long>{
}
