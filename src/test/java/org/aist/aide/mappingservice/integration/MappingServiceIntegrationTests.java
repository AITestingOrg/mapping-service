package org.aist.aide.mappingservice.integration;

import static org.aist.aide.mappingservice.utils.TestsConstants.*;

import org.aist.aide.mappingservice.service.controllers.MappingController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MappingServiceIntegrationTests {

    @Autowired
    private MappingController mappingController;

    @After
    public void tearDown() {
        var everything = mappingController.getMappings().getBody();
        for (var mapping: everything) {
            mappingController.deleteMapping(mapping.getId());
        }
    }

    @Test
    public void givenDbEmpty_WhenCreateIsCalled_EntryIsCreated() {
        //arrange
        var resultsEmpty = mappingController.getMappings().getBody();

        //act
        mappingController.createMapping(mapping);

        //assert
        var resultsInDB = mappingController.getMappings().getBody();
        Assert.assertTrue(resultsEmpty.isEmpty());
        Assert.assertFalse(resultsInDB.isEmpty());
    }

    @Test
    public void givenItemNotInDb_WhenGetIsCalled_RespondNotFound() {
        //arrange

        //act
        var result = mappingController.getMapping(label, abstraction);

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemInDb_WhenGetIsCalled_RespondOkAndItemRetrieved() {
        //arrange
        mappingController.createMapping(mapping);

        //act
        var result = mappingController.getMapping(label,type);

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().compareTo(mapping));
    }

    @Test
    public void givenItemInDb_WhenGetKnownIsCalled_ReturnOnlyKnown() {
        //arrange
        mappingController.createMapping(mapping);
        mappingController.createMapping(mapping2);

        //act
        var result = mappingController.getKnownMappings();

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().get(0).compareTo(mapping));
    }

    @Test
    public void givenItemInDb_WhenGetUnknownIsCalled_ReturnOnlyUnknown() {
        //arrange
        mappingController.createMapping(mapping);
        mappingController.createMapping(mapping2);

        //act
        var result = mappingController.getUnknownMappings();

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().get(0).compareTo(mapping2));
    }

    @Test
    public void givenItemInDb_WhenDeleteIsCalled_ItemIsRemoved() {
        //arrange
        mappingController.createMapping(mapping);
        var resultInDB = mappingController.getMapping(label,type);

        //act
        mappingController.deleteMapping(resultInDB.getBody().getId());

        //assert
        Assert.assertEquals(resultInDB.getStatusCode(), HttpStatus.OK);
        var resultNotInDB = mappingController.getMapping(label,type);
        Assert.assertEquals(resultNotInDB.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemNotInDb_WhenDeleteIsCalled_RespondNotFound() {
        //arrange

        //act
        var response = mappingController.deleteMapping(999);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemInDb_WhenUpdateIsCalled_ItemIsUpdated() {
        //arrange
        mappingController.createMapping(mapping);
        var mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingInDB.setLabel(label2);
        mappingInDB.setAbstraction(abstraction2);

        //act
        mappingController.updateMapping(mappingInDB);

        //assert
        var updatedMapping = mappingController.getMapping(label2,type2);
        Assert.assertEquals(updatedMapping.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(updatedMapping.getBody().compareTo(mappingInDB));
    }

    @Test
    public void givenItemInDb_WhenUpdateIsCalledToUpdateToSameLabelType_RespondBadRequest() {
        //arrange
        mappingController.createMapping(mapping);
        mappingController.createMapping(mapping2);
        var mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingInDB.setLabel(label2);
        mappingInDB.setAbstraction(abstraction2);

        //act
        var response = mappingController.updateMapping(mappingInDB);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenItemNotInDb_WhenUpdateIsCalled_RespondNotFound() {
        //arrange
        mappingController.createMapping(mapping);
        var mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingController.deleteMapping(mappingInDB.getId());

        //act
        var response = mappingController.updateMapping(mappingInDB);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
