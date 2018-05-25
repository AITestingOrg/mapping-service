package org.aist.aide.mappingservice.domain.services;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.aist.aide.mappingservice.domain.exceptions.NotFoundException;
import org.aist.aide.mappingservice.domain.exceptions.ValidationFailureException;
import org.aist.aide.mappingservice.domain.models.Classifier;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.aist.aide.mappingservice.service.repositories.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MappingCrudService {
    private static final Logger LOGGER = Logger.getLogger(MappingCrudService.class.getName());
    private MappingRepository mappingRepository;

    public MappingCrudService(@Autowired MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    public List<Mapping> getMappings() {
        return mappingRepository.findAll();
    }

    public List<Mapping> getKnown() {
        var mappings = getMappings();
        return mappings.stream().filter(x -> !x.getClassifiers().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Mapping> getUnknown() {
        var mappings = getMappings();
        return mappings.stream().filter(x -> x.getClassifiers().isEmpty())
                .collect(Collectors.toList());
    }

    public Mapping getMapping(String label, String type) throws NotFoundException {
        var mapping = mappingRepository.findByLabelAndType(label, type);
        if (mapping.isPresent()) {
            return mapping.get();
        }
        LOGGER.warning(String.format("Mapping for label %s and type %s does not exist.", label, type));
        throw new NotFoundException(
                String.format("Failed to find mapping for label %s and type %s does.", label,  type));
    }

    public Mapping getMapping(String id) throws NotFoundException {
        var mapping = mappingRepository.findById(id);
        if (mapping.isPresent()) {
            return mapping.get();
        }
        LOGGER.warning(String.format("Mapping for id %s does not exist.", id));
        throw new NotFoundException(
                String.format("Failed to find mapping for id %s.", id));
    }

    public void createMapping(Mapping mapping) throws ValidationFailureException {
        var label = mapping.getLabel();
        var type = mapping.getType();
        var existingMapping = mappingRepository.findByLabelAndType(label, type);
        if (existingMapping.isPresent()) {
            LOGGER.warning(String.format("Mapping for label %s and type %s already exists.", label, type));
            throw new ValidationFailureException(
                    String.format("mapping for label %s and type %s already exists, cannot create.", label, type));
        }
        mappingRepository.save(mapping);
    }

    public void updateMapping(Mapping mapping) throws NotFoundException, ValidationFailureException {
        final var id = mapping.getId();
        final var label = mapping.getLabel();
        final var type = mapping.getType();
        var mappingToUpdate = mappingRepository.findById(id);
        if (!mappingToUpdate.isPresent()) {
            LOGGER.warning(String.format("Mapping with id %s does not exist, cannot update.", mapping.getId()));
            throw new NotFoundException(String.format("No mapping found with id %s", mapping.getId()));
        }
        mappingToUpdate = mappingRepository.findByLabelAndType(label, type);
        if (mappingToUpdate.isPresent()) {
            if (!mappingToUpdate.get().getId().equals(id)) {
                LOGGER.warning(
                        String.format("Different mapping for label %s and type %s already exists.", label, type));
                throw new ValidationFailureException(
                        String.format("Mapping for label %s and type %s already exists, cannot update.", label, type));
            }
        }
        mappingRepository.save(mapping);
    }

    public void upsertClassifier(String id, Classifier classifier)
            throws NotFoundException {
        var mappingToUpdate = mappingRepository.findById(id);
        if (!mappingToUpdate.isPresent()) {
            LOGGER.warning(String.format("Mapping with id %s does not exist, cannot update.", id));
            throw new NotFoundException(String.format("No mapping found with id %s", id));
        }
        var mapping = mappingToUpdate.get();
        mapping.upsertClassifier(classifier);
        mappingRepository.save(mapping);
    }


    public void deleteMapping(String id) throws NotFoundException {
        var mapping = mappingRepository.findById(id);
        if (!mapping.isPresent()) {
            LOGGER.warning(String.format("Mapping with id %s does not exist, cannot delete.", id));
            throw new NotFoundException(String.format("No mapping found with id %s", id));
        }
        mappingRepository.delete(mapping.get());
    }
}
