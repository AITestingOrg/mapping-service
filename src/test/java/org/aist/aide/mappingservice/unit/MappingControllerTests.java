package org.aist.aide.mappingservice.unit;

import static org.aist.aide.mappingservice.utils.TestsConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.aist.aide.mappingservice.domain.exceptions.NotFoundException;
import org.aist.aide.mappingservice.domain.exceptions.ValidationFailureException;
import org.aist.aide.mappingservice.domain.services.MappingCrudService;
import org.aist.aide.mappingservice.service.controllers.MappingController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MappingControllerTests {

    @Mock
    private MappingCrudService mappingCrudService;

    @InjectMocks
    private MappingController mappingController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAGetAllRequest_ThenServiceGetMappingsIsCalled() {
        // arrange

        // act
        mappingController.getMappings();

        // assert
        verify(mappingCrudService, times(1)).getMappings();
    }

    @Test
    public void givenACreateRequest_ThenServiceCreateIsCalled() throws ValidationFailureException {
        // act
        mappingController.createMapping(mapping);

        // assert
        verify(mappingCrudService, times(1)).createMapping(mapping);
    }

    @Test
    public void givenADeleteRequest_ThenServiceDeleteIsCalled()throws NotFoundException {
        // act
        mappingController.deleteMapping(1);

        // assert
        verify(mappingCrudService, times(1)).deleteMapping(1);
    }

    @Test
    public void givenAnUpdateRequest_ThenServiceUpdateIsCalled() throws ValidationFailureException, NotFoundException {
        // act
        mappingController.updateMapping(mapping);

        // assert
        verify(mappingCrudService, times(1)).updateMapping(mapping);
    }
}
