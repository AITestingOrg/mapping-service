package org.aist.aide.mappingservice.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.TreeSet;
import javax.validation.constraints.NotBlank;
import org.aist.aide.formexpert.common.models.Classifier;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mapping extends org.aist.aide.formexpert.common.models.Mapping {
    @Id
    private String id;

    public Mapping(String id, @NotBlank String label, @NotBlank String type,
                   TreeSet<Classifier> classifiers, String defaultAbstraction) {
        super(id, label, type, classifiers, defaultAbstraction);
    }

    public Mapping(@NotBlank String label, @NotBlank String type) {
        super(label, type);
    }

    public Mapping() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDefaultAbstraction(String defaultAbstraction) {
        this.defaultAbstraction = defaultAbstraction;
    }

    public void setClassifiers(TreeSet<Classifier> classifiers) {
        this.classifiers = classifiers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Mapping)) {
            return false;
        }
        var other = (Mapping) obj;

        return (other.getLabel().equals(label) && other.getType().equals(type));
    }
}
