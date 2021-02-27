package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.polytech.polysign.domain.enumeration.Status;

/**
 * A SignatureProcess.
 */
@Entity
@Table(name = "signature_process")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SignatureProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "emission_date", nullable = false)
    private Instant emissionDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "ordered_signing", nullable = false)
    private Boolean orderedSigning;

    @OneToMany(mappedBy = "signature")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SignedFile> files = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "signatures", allowSetters = true)
    private AuthenticatedUser creator;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEmissionDate() {
        return emissionDate;
    }

    public SignatureProcess emissionDate(Instant emissionDate) {
        this.emissionDate = emissionDate;
        return this;
    }

    public void setEmissionDate(Instant emissionDate) {
        this.emissionDate = emissionDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public SignatureProcess expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getTitle() {
        return title;
    }

    public SignatureProcess title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public SignatureProcess status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean isOrderedSigning() {
        return orderedSigning;
    }

    public SignatureProcess orderedSigning(Boolean orderedSigning) {
        this.orderedSigning = orderedSigning;
        return this;
    }

    public void setOrderedSigning(Boolean orderedSigning) {
        this.orderedSigning = orderedSigning;
    }

    public Set<SignedFile> getFiles() {
        return files;
    }

    public SignatureProcess files(Set<SignedFile> signedFiles) {
        this.files = signedFiles;
        return this;
    }

    public SignatureProcess addFile(SignedFile signedFile) {
        this.files.add(signedFile);
        signedFile.setSignature(this);
        return this;
    }

    public SignatureProcess removeFile(SignedFile signedFile) {
        this.files.remove(signedFile);
        signedFile.setSignature(null);
        return this;
    }

    public void setFiles(Set<SignedFile> signedFiles) {
        this.files = signedFiles;
    }

    public AuthenticatedUser getCreator() {
        return creator;
    }

    public SignatureProcess creator(AuthenticatedUser authenticatedUser) {
        this.creator = authenticatedUser;
        return this;
    }

    public void setCreator(AuthenticatedUser authenticatedUser) {
        this.creator = authenticatedUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignatureProcess)) {
            return false;
        }
        return id != null && id.equals(((SignatureProcess) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignatureProcess{" +
            "id=" + getId() +
            ", emissionDate='" + getEmissionDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", status='" + getStatus() + "'" +
            ", orderedSigning='" + isOrderedSigning() + "'" +
            "}";
    }
}
