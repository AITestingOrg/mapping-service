package org.aist.aide.mappingservice.service.repositories;

import java.util.Optional;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MappingRepository extends CrudRepository<Mapping, Long> {
    Optional<Mapping> findByLabelAndType(String label, String type);
}
