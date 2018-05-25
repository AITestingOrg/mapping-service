package org.aist.aide.mappingservice.service.repositories;

import java.util.Optional;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MappingRepository extends MongoRepository<Mapping, String> {
    Optional<Mapping> findByLabelAndType(String label, String type);
}
