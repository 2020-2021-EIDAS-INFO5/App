package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A SignedFile.
 */
@Entity
@Table(name = "signed_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SignedFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @Lob
    @Column(name = "file_bytes")
    private byte[] fileBytes;

    @Column(name = "file_bytes_content_type")
    private String fileBytesContentType;

    @NotNull
    @Column(name = "signing_date", nullable = false)
    private Instant signingDate;

    @Column(name = "size")
    private Integer size;

    @ManyToOne
    @JsonIgnoreProperties(value = "files", allowSetters = true)
    private SignatureProcess signature;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public SignedFile filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public SignedFile fileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
        return this;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFileBytesContentType() {
        return fileBytesContentType;
    }

    public SignedFile fileBytesContentType(String fileBytesContentType) {
        this.fileBytesContentType = fileBytesContentType;
        return this;
    }

    public void setFileBytesContentType(String fileBytesContentType) {
        this.fileBytesContentType = fileBytesContentType;
    }

    public Instant getSigningDate() {
        return signingDate;
    }

    public SignedFile signingDate(Instant signingDate) {
        this.signingDate = signingDate;
        return this;
    }

    public void setSigningDate(Instant signingDate) {
        this.signingDate = signingDate;
    }

    public Integer getSize() {
        return size;
    }

    public SignedFile size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public SignatureProcess getSignature() {
        return signature;
    }

    public SignedFile signature(SignatureProcess signatureProcess) {
        this.signature = signatureProcess;
        return this;
    }

    public void setSignature(SignatureProcess signatureProcess) {
        this.signature = signatureProcess;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignedFile)) {
            return false;
        }
        return id != null && id.equals(((SignedFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignedFile{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", fileBytes='" + getFileBytes() + "'" +
            ", fileBytesContentType='" + getFileBytesContentType() + "'" +
            ", signingDate='" + getSigningDate() + "'" +
            ", size=" + getSize() +
            "}";
    }
}
