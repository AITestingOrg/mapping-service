package org.aist.aide.mappingservice.domain.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "mapping",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"label", "type"})})
public class Mapping implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String label;

    @NotBlank
    private String type;

    private String abstraction;

    public Mapping() {
    }

    public Mapping(@NotBlank String label, @NotBlank String type, String abstraction) {
        this.label = label;
        this.type = type;
        this.abstraction = abstraction;
    }

    public Mapping(@NotBlank String label, @NotBlank String type) {
        this.label = label;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getAbstraction() {
        return abstraction;
    }
}
