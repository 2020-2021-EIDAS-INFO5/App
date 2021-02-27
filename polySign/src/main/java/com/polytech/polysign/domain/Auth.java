package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.polytech.polysign.domain.enumeration.Role;

/**
 * A Auth.
 */
@Entity
@Table(name = "auth")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Auth implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "has_role", nullable = false)
    private Role hasRole;

    @ManyToOne
    @JsonIgnoreProperties(value = "authorities", allowSetters = true)
    private AuthenticatedUser authenticatedUser;

    @ManyToOne
    @JsonIgnoreProperties(value = "auths", allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getHasRole() {
        return hasRole;
    }

    public Auth hasRole(Role hasRole) {
        this.hasRole = hasRole;
        return this;
    }

    public void setHasRole(Role hasRole) {
        this.hasRole = hasRole;
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public Auth authenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        return this;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Auth organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Auth)) {
            return false;
        }
        return id != null && id.equals(((Auth) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Auth{" +
            "id=" + getId() +
            ", hasRole='" + getHasRole() + "'" +
            "}";
    }
}
