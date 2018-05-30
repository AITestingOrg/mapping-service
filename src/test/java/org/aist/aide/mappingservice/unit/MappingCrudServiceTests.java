package org.aist.aide.mappingservice.unit;

import static org.aist.aide.mappingservice.utils.TestsConstants.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.util.Optional;

import org.aist.aide.formexpert.common.exceptions.NotFoundException;
import org.aist.aide.formexpert.common.exceptions.ValidationFailureException;
import org.aist.aide.mappingservice.domain.services.MappingCrudService;
import org.aist.aide.mappingservice.service.repositories.MappingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MappingCrudServiceTests {
    @Mock
    private MappingRepository mappingRepository;

    @InjectMocks
    private MappingCrudService mappingCrudService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotFoundException.class)
    public void givenAMappingDoesNotExists_WhenFindCalled_ExceptionIsThrown() throws NotFoundException {
        // act
        mappingCrudService.getMapping(label, type);
    }

    @Test(expected = NotFoundException.class)
    public void givenAMappingDoesNotExists_WhenFindByIdCalled_ExceptionIsThrown() throws NotFoundException {
        // act
        mappingCrudService.getMapping(id);
    }

    @Test
    public void givenAMappingExists_WhenFindCalled_TheMappingIsReturned() throws NotFoundException {
        // arrange
        when(mappingRepository.findByLabelAndType(label, type)).thenReturn(Optional.of(mapping));

        // act
        var mappingFound = mappingCrudService.getMapping(label, type);

        // assert
        Assert.assertEquals(mapping, mappingFound);
    }

    @Test
    public void givenAMappingExists_WhenFindByIdCalled_TheMappingIsReturned() throws NotFoundException {
        // arrange
        when(mappingRepository.findById(id)).thenReturn(Optional.of(mapping));

        // act
        var mappingFound = mappingCrudService.getMapping(id);

        // assert
        Assert.assertEquals(mapping, mappingFound);
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenDeleteCalled_ExceptionIsThrown() throws NotFoundException {
        // act
        mappingCrudService.deleteMapping("asda");
    }

    @Test
    public void givenAMappingExists_WhenDeleteCalled_NoExceptionIsThrownAndDeleteIsCalled() throws NotFoundException {
        // arrange
        when(mappingRepository.findById((mapping.getId()))).thenReturn(Optional.of(mapping));

        // act
        mappingCrudService.deleteMapping(mapping.getId());

        // assert
        verify(mappingRepository,times(1)).delete(mapping);
    }

    @Test
    public void givenNoMappingExists_WhenCreateCalled_SaveIsCalled() throws ValidationFailureException {
        // act
        mappingCrudService.createMapping(mapping);

        // assert
        verify(mappingRepository,times(1)).save(mapping);
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenUpdateCalled_NotFoundExceptionIsThrown()
            throws ValidationFailureException, NotFoundException {
        // act
        mappingCrudService.updateMapping(mapping);
    }

    @Test
    public void givenAMappingExists_WhenUpdateCalled_SaveIsCalled()
            throws ValidationFailureException, NotFoundException {
        // arrange
        when(mappingRepository.findById(mapping.getId())).thenReturn(Optional.of(mapping));

        // act
        mappingCrudService.updateMapping(mapping);

        // assert
        verify(mappingRepository,times(1)).save(mapping);
    }

    @Test
    public void givenAGetMappings_ThenFindAllIsCalled() {
        // act
        mappingCrudService.getMappings();

        // assert
        verify(mappingRepository,times(1)).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenUpsert_NotFoundExceptionIsThrown() throws NotFoundException {
        // act
        mappingCrudService.upsertClassifier(id2, classifier);
    }

    @Test
    public void givenMappingExists_WhenUpsert_ThenFindByIdCalled() throws NotFoundException {
        // arrange
        when(mappingRepository.findById(mapping2.getId())).thenReturn(Optional.of(mapping));

        // act
        mappingCrudService.upsertClassifier(mapping2.getId(), classifier);

        // assert
        verify(mappingRepository,times(1)).findById(mapping2.getId());
    }
}
