package org.aist.aide.mappingservice.unit;

import static org.aist.aide.mappingservice.utils.TestsConstants.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.util.Optional;
import org.aist.aide.mappingservice.domain.exceptions.NotFoundException;
import org.aist.aide.mappingservice.domain.exceptions.ValidationFailureException;
import org.aist.aide.mappingservice.domain.models.Mapping;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
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

    @Test
    public void givenAMappingExists_WhenFindCalled_TheMappingIsReturned() throws NotFoundException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);
        when(mappingRepository.findByLabelAndType(label, type)).thenReturn(Optional.of(mapping));

        // act
        var mappingFound = mappingCrudService.getMapping(label, type);

        // assert
        Assert.assertEquals(mapping, mappingFound);
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenDeleteCalled_ExceptionIsThrown() throws NotFoundException {
        // act
        mappingCrudService.deleteMapping(1);
    }

    @Test
    public void givenAMappingExists_WhenDeleteCalled_NoExceptionIsThrownAndDeleteIsCalled() throws NotFoundException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);
        when(mappingRepository.findById((long) 1)).thenReturn(Optional.of(mapping));

        // act
        mappingCrudService.deleteMapping(1);

        // assert
        verify(mappingRepository,times(1)).delete(mapping);
    }

    @Test(expected = ValidationFailureException.class)
    public void givenAMappingExists_WhenCreateCalled_ExceptionIsThrown() throws ValidationFailureException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);
        when(mappingRepository.findByLabelAndType(label, type)).thenReturn(Optional.of(mapping));

        // act
        mappingCrudService.createMapping(mapping);
    }

    @Test
    public void givenNoMappingExists_WhenCreateCalled_SaveIsCalled() throws ValidationFailureException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);

        // act
        mappingCrudService.createMapping(mapping);

        // assert
        verify(mappingRepository,times(1)).save(mapping);
    }

    @Test(expected = ValidationFailureException.class)
    public void givenAMappingAlreadyExists_WhenUpdateCalled_ExceptionIsThrown()
            throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(1, label, type, abstraction);
        var mapping2 = new Mapping(2, label, type, abstraction2);
        when(mappingRepository.findByLabelAndType(label, type)).thenReturn(Optional.of(mapping));
        when(mappingRepository.findById((long) 2)).thenReturn(Optional.of(mapping2));

        // act
        mappingCrudService.updateMapping(mapping2);
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenUpdateCalled_NotFoundExceptionIsThrown()
            throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);

        // act
        mappingCrudService.updateMapping(mapping);
    }

    @Test
    public void givenAMappingExists_WhenUpdateCalled_SaveIsCalled()
            throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(label, type, abstraction);
        when(mappingRepository.findById((long) 0)).thenReturn(Optional.of(mapping));

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
}
