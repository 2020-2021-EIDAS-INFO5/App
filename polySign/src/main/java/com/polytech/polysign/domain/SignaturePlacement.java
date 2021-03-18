package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SignaturePlacement.
 */
@Entity
@Table(name = "signature_placement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SignaturePlacement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "coordinate_x")
    private Double coordinateX;

    @Column(name = "coordinate_y")
    private Double coordinateY;

    @ManyToOne
    @JsonIgnoreProperties(value = "signaturePlacements", allowSetters = true)
    private SignOrder placement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public SignaturePlacement pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public SignaturePlacement coordinateX(Double coordinateX) {
        this.coordinateX = coordinateX;
        return this;
    }

    public void setCoordinateX(Double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }

    public SignaturePlacement coordinateY(Double coordinateY) {
        this.coordinateY = coordinateY;
        return this;
    }

    public void setCoordinateY(Double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public SignOrder getPlacement() {
        return placement;
    }

    public SignaturePlacement placement(SignOrder signOrder) {
        this.placement = signOrder;
        return this;
    }

    public void setPlacement(SignOrder signOrder) {
        this.placement = signOrder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignaturePlacement)) {
            return false;
        }
        return id != null && id.equals(((SignaturePlacement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignaturePlacement{" +
            "id=" + getId() +
            ", pageNumber=" + getPageNumber() +
            ", coordinateX=" + getCoordinateX() +
            ", coordinateY=" + getCoordinateY() +
            "}";
    }
}
