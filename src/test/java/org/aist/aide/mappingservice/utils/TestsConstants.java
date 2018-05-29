package org.aist.aide.mappingservice.utils;

import java.util.Arrays;
import java.util.TreeSet;
import org.aist.aide.mappingservice.domain.models.Classifier;
import org.aist.aide.mappingservice.domain.models.Mapping;
import org.aist.aide.mappingservice.domain.models.Services;

public class TestsConstants {

    public static final String id = "id";
    public static final String label = "label";
    public static final String type = "type";
    public static final String abstraction = "abstraction";
    public static final Classifier classifier = new Classifier(Services.APISERVICE, 0.0);
    public static final Classifier classifierHighScore = new Classifier(Services.APISERVICE, 0.9);
    private static TreeSet<Classifier> classifiers = new TreeSet<>(Arrays.asList(classifier));
    public static final Mapping mapping = new Mapping(id, label, type, classifiers, abstraction);

    public static final String id2 = "id2";
    public static final String label2 = "label2";
    public static final String type2 = "type2";
    public static final Mapping mapping2 = new Mapping(label2, type2);

}
