package org.aist.aide.mappingservice.integration;

import static org.aist.aide.mappingservice.utils.TestsConstants.*;

import java.util.List;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.aist.aide.mappingservice.service.controllers.MappingController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class MappingServiceIntegrationTests {

    @Autowired
    private MappingController mappingController;

    @After
    public void tearDown() {
        mappingController.deleteMapping(1);
        mappingController.deleteMapping(2);
    }

    @Test
    public void givenDbEmpty_WhenCreateIsCalled_EntryIsCreated() {
        //arrange
        List<Mapping> resultsEmpty = mappingController.getMappings().getBody();

        //act
        mappingController.createMapping(mapping);

        //assert
        List<Mapping> resultsInDB = mappingController.getMappings().getBody();
        Assert.assertTrue(resultsEmpty.isEmpty());
        Assert.assertFalse(resultsInDB.isEmpty());
    }

    @Test
    public void givenItemNotInDb_WhenGetIsCalled_RespondNotFound() {
        //arrange

        //act
        ResponseEntity<Mapping> result = mappingController.getMapping(label, abstraction);

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemInDb_WhenGetIsCalled_RespondOkAndItemRetrieved() {
        //arrange
        mappingController.createMapping(mapping);

        //act
        ResponseEntity<Mapping> result = mappingController.getMapping(label,type);

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().compareTo(mapping));
    }

    @Test
    public void givenItemInDb_WhenDeleteIsCalled_ItemIsRemoved() {
        //arrange
        mappingController.createMapping(mapping);
        ResponseEntity<Mapping> resultInDB = mappingController.getMapping(label,type);

        //act
        mappingController.deleteMapping(resultInDB.getBody().getId());

        //assert
        Assert.assertEquals(resultInDB.getStatusCode(), HttpStatus.OK);
        ResponseEntity<Mapping> resultNotInDB = mappingController.getMapping(label,type);
        Assert.assertEquals(resultNotInDB.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemNotInDb_WhenDeleteIsCalled_RespondNotFound() {
        //arrange

        //act
        ResponseEntity response = mappingController.deleteMapping(999);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemInDb_WhenUpdateIsCalled_ItemIsUpdated() {
        //arrange
        mappingController.createMapping(mapping);
        Mapping mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingInDB.setLabel(label2);
        mappingInDB.setAbstraction(abstraction2);

        //act
        mappingController.updateMapping(mappingInDB);

        //assert
        ResponseEntity<Mapping> updatedMapping = mappingController.getMapping(label2,type2);
        Assert.assertEquals(updatedMapping.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(updatedMapping.getBody().compareTo(mapping2));
    }

    @Test
    public void givenItemInDb_WhenUpdateIsCalledToUpdateToSameLabelType_RespondBadRequest() {
        //arrange
        mappingController.createMapping(mapping);
        mappingController.createMapping(mapping2);
        Mapping mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingInDB.setLabel(label2);
        mappingInDB.setAbstraction(abstraction2);

        //act
        ResponseEntity response = mappingController.updateMapping(mappingInDB);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenItemNotInDb_WhenUpdateIsCalled_RespondNotFound() {
        //arrange
        mappingController.createMapping(mapping);
        Mapping mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingController.deleteMapping(mappingInDB.getId());

        //act
        ResponseEntity response = mappingController.updateMapping(mappingInDB);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
