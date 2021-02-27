package com.polytech.polysign.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AuthenticatedUser.
 */
@Entity
@Table(name = "authenticated_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthenticatedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SignatureProcess> signatures = new HashSet<>();

    @OneToMany(mappedBy = "authenticatedUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Auth> authorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public AuthenticatedUser user(UserEntity userEntity) {
        this.user = userEntity;
        return this;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
    }

    public Set<SignatureProcess> getSignatures() {
        return signatures;
    }

    public AuthenticatedUser signatures(Set<SignatureProcess> signatureProcesses) {
        this.signatures = signatureProcesses;
        return this;
    }

    public AuthenticatedUser addSignature(SignatureProcess signatureProcess) {
        this.signatures.add(signatureProcess);
        signatureProcess.setCreator(this);
        return this;
    }

    public AuthenticatedUser removeSignature(SignatureProcess signatureProcess) {
        this.signatures.remove(signatureProcess);
        signatureProcess.setCreator(null);
        return this;
    }

    public void setSignatures(Set<SignatureProcess> signatureProcesses) {
        this.signatures = signatureProcesses;
    }

    public Set<Auth> getAuthorities() {
        return authorities;
    }

    public AuthenticatedUser authorities(Set<Auth> auths) {
        this.authorities = auths;
        return this;
    }

    public AuthenticatedUser addAuthority(Auth auth) {
        this.authorities.add(auth);
        auth.setAuthenticatedUser(this);
        return this;
    }

    public AuthenticatedUser removeAuthority(Auth auth) {
        this.authorities.remove(auth);
        auth.setAuthenticatedUser(null);
        return this;
    }

    public void setAuthorities(Set<Auth> auths) {
        this.authorities = auths;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticatedUser)) {
            return false;
        }
        return id != null && id.equals(((AuthenticatedUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthenticatedUser{" +
            "id=" + getId() +
            "}";
    }
}
