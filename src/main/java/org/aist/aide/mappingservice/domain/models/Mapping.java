package org.aist.aide.mappingservice.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mapping implements Serializable {
    @Id
    private String id;

    @NotBlank
    private String label;

    @NotBlank
    private String type;

    private TreeSet<Classifier> classifiers;

    private String defaultAbstraction;

    public Mapping() {
        classifiers = new TreeSet<>();
    }

    public Mapping(String id, @NotBlank String label, @NotBlank String type,
                   TreeSet<Classifier> classifiers, String defaultAbstraction) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.classifiers = classifiers;
        this.defaultAbstraction = defaultAbstraction;
    }

    public Mapping(@NotBlank String label, @NotBlank String type) {
        this.label = label;
        this.type = type;
        classifiers = new TreeSet<>();
    }

    public String getDefaultAbstraction() {
        return defaultAbstraction;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public Set<Classifier> getClassifiers() {
        return classifiers;
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

    public void upsertClassifier(Classifier classifier) {
        for (var storedClassifier : classifiers) {
            if (storedClassifier.getService() == classifier.getService()) {
                classifiers.remove(storedClassifier);
                classifiers.add(classifier);
                return;
            }
        }
        classifiers.add(classifier);
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
