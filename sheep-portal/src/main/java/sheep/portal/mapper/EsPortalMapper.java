package sheep.portal.mapper;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import sheep.portal.entity.EsPortal;

import java.util.List;

public interface EsPortalMapper extends ElasticsearchRepository<EsPortal, Integer> {
}
