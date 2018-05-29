package org.aist.aide.mappingservice.domain.models;

import javax.validation.constraints.NotBlank;

public class Classifier implements Comparable<Classifier> {
    @NotBlank
    private Services service;
    private double score;

    public Classifier(Services service, double score) {
        this.service = service;
        this.score = score;
    }

    public Services getService() {
        return service;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(Classifier o) {
        var result = score - o.getScore();
        if (result < 0) {
            return (int) Math.floor(result);
        } else {
            return (int) Math.ceil(result);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Classifier)) {
            return false;
        }
        var other = (Classifier) obj;

        return (other.getScore() == score && other.getService() == service);
    }
}
