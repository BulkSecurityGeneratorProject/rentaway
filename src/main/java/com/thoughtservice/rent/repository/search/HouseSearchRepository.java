package com.thoughtservice.rent.repository.search;

import com.thoughtservice.rent.domain.House;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the House entity.
 */
public interface HouseSearchRepository extends ElasticsearchRepository<House, Long> {
}
