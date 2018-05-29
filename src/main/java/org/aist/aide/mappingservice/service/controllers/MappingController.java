package org.aist.aide.mappingservice.service.controllers;

import java.util.List;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.aist.aide.mappingservice.domain.exceptions.NotFoundException;
import org.aist.aide.mappingservice.domain.exceptions.ValidationFailureException;
import org.aist.aide.mappingservice.domain.models.Classifier;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.aist.aide.mappingservice.domain.services.MappingCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/mapping")
public class MappingController {
    private static final Logger LOGGER = Logger.getLogger(MappingController.class.getName());
    private MappingCrudService mappingCrudService;

    public MappingController(@Autowired MappingCrudService mappingCrudService) {
        this.mappingCrudService = mappingCrudService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Mapping>> getMappings() {
        LOGGER.info("GET request for all Mappings.");
        var mappings = mappingCrudService.getMappings();
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mapping> getMapping(@PathVariable String id) {
        LOGGER.info(String.format("GET request for Mapping with id %s.", id));
        try {
            var mapping = mappingCrudService.getMapping(id);
            return new ResponseEntity<>(mapping, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/{label}/{type}")
    public ResponseEntity<Mapping> getMapping(@PathVariable String label, @PathVariable String type) {
        LOGGER.info(String.format("GET request for Mapping of label %s and type %s.", label, type));
        try {
            var mapping = mappingCrudService.getMapping(label, type);
            return new ResponseEntity<>(mapping, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/known")
    public ResponseEntity<List<Mapping>> getKnownMappings() {
        LOGGER.info("GET request for all known Mappings.");
        var knownMappings = mappingCrudService.getKnown();
        return new ResponseEntity<>(knownMappings, HttpStatus.OK);
    }

    @GetMapping("/unknown")
    public ResponseEntity<List<Mapping>> getUnknownMappings() {
        LOGGER.info("GET request for all unknown Mappings.");
        var unknownMappings = mappingCrudService.getUnknown();
        return new ResponseEntity<>(unknownMappings, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public ResponseEntity createMapping(@Valid @RequestBody Mapping mapping) {
        LOGGER.info(String.format("POST request with Mapping %s.", mapping));
        try {
            mappingCrudService.createMapping(mapping);
        } catch (ValidationFailureException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mapping.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMapping(@PathVariable String id) {
        LOGGER.info(String.format("DELETE request for Mapping with id %s.", id));
        try {
            mappingCrudService.deleteMapping(id);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(path = "/")
    public ResponseEntity updateMapping(@Valid @RequestBody Mapping mapping) {
        LOGGER.info(String.format("PUT request for Mapping %s.", mapping));
        try {
            mappingCrudService.updateMapping(mapping);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (ValidationFailureException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateClassifier(@Valid @PathVariable String id,
                                           @Valid @RequestBody Classifier classifier) {
        LOGGER.info(String.format("PUT request for Mapping %s.", id));
        try {
            mappingCrudService.upsertClassifier(id, classifier);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
