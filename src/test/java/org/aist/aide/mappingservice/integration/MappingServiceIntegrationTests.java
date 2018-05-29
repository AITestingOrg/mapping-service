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
        //act
        var result = mappingController.getMapping(label, type);

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemNotInDb_WhenGetByIdIsCalled_RespondNotFound() {
        //act
        var result = mappingController.getMapping(id);

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
        Assert.assertEquals(result.getBody(), mapping);
    }

    @Test
    public void givenItemInDb_WhenGetByIsCalled_RespondOkAndItemRetrieved() {
        //arrange
        mappingController.createMapping(mapping);

        //act
        var result = mappingController.getMapping(mapping.getId());

        //assert
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(result.getBody(), mapping);
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
        Assert.assertEquals(result.getBody().get(0), mapping);
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
        Assert.assertEquals(result.getBody().get(0), mapping2);
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
        //act
        var response = mappingController.deleteMapping(id);

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

        //act
        mappingController.updateMapping(mappingInDB);

        //assert
        var updatedMapping = mappingController.getMapping(label2,type2);
        Assert.assertEquals(updatedMapping.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(updatedMapping.getBody(), mappingInDB);
    }

    @Test
    public void givenItemInDb_WhenUpdateIsCalledToUpdateToSameLabelType_RespondBadRequest() {
        //arrange
        mappingController.createMapping(mapping);
        mappingController.createMapping(mapping2);
        var mappingInDB = mappingController.getMapping(label,type).getBody();
        mappingInDB.setType(type2);
        mappingInDB.setLabel(label2);

        //act
        var response = mappingController.updateMapping(mappingInDB);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenItemNotInDb_WhenUpdateIsCalled_RespondNotFound() {
        //act
        var response = mappingController.updateMapping(mapping);

        //assert
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenItemWithNoClassifier_WhenUpdateClassifierIsCalled_ClassifierIsAdded() {
        //arrange
        mappingController.createMapping(mapping2);

        //act
        mappingController.updateClassifier(mapping2.getId(), classifier);

        //assert
        var response = mappingController.getMapping(mapping2.getId());
        Assert.assertTrue(response.getBody().getClassifiers().contains(classifier));
    }

    @Test
    public void givenItemWithClassifier_WhenUpdateClassifierIsCalled_ClassifierIsUpdated() {
        //arrange
        mappingController.createMapping(mapping);

        //act
        mappingController.updateClassifier(mapping.getId(), classifierHighScore);

        //assert
        var response = mappingController.getMapping(mapping.getId());
        Assert.assertTrue(response.getBody().getClassifiers().contains(classifierHighScore));
    }
}
