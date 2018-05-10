package org.aist.aide.mappingservice.unit;

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
    private MappingRepository patternRepository;

    @InjectMocks
    private MappingCrudService patternCrudService;


    private final String LABEL = "LABEL";
    private final String TYPE = "type";
    private final String ABSTRACTION = "ABSTRACTION";

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotFoundException.class)
    public void givenAMappingDoesNotExists_WhenFindCalled_ExceptionIsThrown() throws NotFoundException {
        // arrange

        // act
        patternCrudService.getMapping(LABEL, TYPE);

        // assert
    }

    @Test
    public void givenAMappingExists_WhenFindCalled_TheMappingIsReturned() throws NotFoundException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);
        when(patternRepository.findByLabelAndType(LABEL, TYPE)).thenReturn(Optional.of(mapping));

        // act
        var mappingFound = patternCrudService.getMapping(LABEL, TYPE);

        // assert
        Assert.assertEquals(mapping, mappingFound);
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenDeleteCalled_ExceptionIsThrown() throws NotFoundException {
        // arrange

        // act
        patternCrudService.deleteMapping(1);

        // assert
    }

    @Test
    public void givenAMappingExists_WhenDeleteCalled_NoExceptionIsThrownAndDeleteIsCalled() throws NotFoundException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);
        when(patternRepository.findById((long) 1)).thenReturn(Optional.of(mapping));

        // act
        patternCrudService.deleteMapping(1);

        // assert
        verify(patternRepository,times(1)).delete(mapping);
    }

    @Test(expected = ValidationFailureException.class)
    public void givenAMappingExists_WhenCreateCalled_ExceptionIsThrown() throws ValidationFailureException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);
        when(patternRepository.findByLabelAndType(LABEL, TYPE)).thenReturn(Optional.of(mapping));

        // act
        patternCrudService.createMapping(mapping);

        // assert
    }

    @Test
    public void givenNoMappingExists_WhenCreateCalled_SaveIsCalled() throws ValidationFailureException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);

        // act
        patternCrudService.createMapping(mapping);

        // assert
        verify(patternRepository,times(1)).save(mapping);
    }

    @Test(expected = ValidationFailureException.class)
    public void givenAMappingAlreadyExists_WhenUpdateCalled_ExceptionIsThrown() throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);
        var mapping2 = new Mapping(LABEL, TYPE, ABSTRACTION);
        when(patternRepository.findByLabelAndType(LABEL, TYPE)).thenReturn(Optional.of(mapping));
        when(patternRepository.findById((long) 0)).thenReturn(Optional.of(mapping));

        // act
        patternCrudService.updateMapping(mapping2);

        // assert
    }

    @Test(expected = NotFoundException.class)
    public void givenNoMappingExists_WhenUpdateCalled_NotFoundExceptionIsThrown() throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);

        // act
        patternCrudService.updateMapping(mapping);

        // assert
    }

    @Test
    public void givenAMappingExists_WhenUpdateCalled_SaveIsCalled() throws ValidationFailureException, NotFoundException {
        // arrange
        var mapping = new Mapping(LABEL, TYPE, ABSTRACTION);
        when(patternRepository.findById((long) 0)).thenReturn(Optional.of(mapping));

        // act
        patternCrudService.updateMapping(mapping);

        // assert
        verify(patternRepository,times(1)).save(mapping);
    }
}
