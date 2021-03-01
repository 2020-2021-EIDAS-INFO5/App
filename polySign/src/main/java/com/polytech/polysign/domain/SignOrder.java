package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SignOrder.
 */
@Entity
@Table(name = "sign_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SignOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_rank")
    private Integer rank;

    @Column(name = "signed")
    private Boolean signed;

    @OneToOne
    @JoinColumn(unique = true)
    private SignedFile file;

    @ManyToOne
    @JsonIgnoreProperties(value = "orders", allowSetters = true)
    private UserEntity signer;

    @ManyToOne
    @JsonIgnoreProperties(value = "signOrders", allowSetters = true)
    private SignatureProcess signature;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public SignOrder rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean isSigned() {
        return signed;
    }

    public SignOrder signed(Boolean signed) {
        this.signed = signed;
        return this;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public SignedFile getFile() {
        return file;
    }

    public SignOrder file(SignedFile signedFile) {
        this.file = signedFile;
        return this;
    }

    public void setFile(SignedFile signedFile) {
        this.file = signedFile;
    }

    public UserEntity getSigner() {
        return signer;
    }

    public SignOrder signer(UserEntity userEntity) {
        this.signer = userEntity;
        return this;
    }

    public void setSigner(UserEntity userEntity) {
        this.signer = userEntity;
    }

    public SignatureProcess getSignature() {
        return signature;
    }

    public SignOrder signature(SignatureProcess signatureProcess) {
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
        if (!(o instanceof SignOrder)) {
            return false;
        }
        return id != null && id.equals(((SignOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignOrder{" +
            "id=" + getId() +
            ", rank=" + getRank() +
            ", signed='" + isSigned() + "'" +
            "}";
    }
}
