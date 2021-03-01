package com.polytech.polysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.polytech.polysign.domain.enumeration.Role;

/**
 * A Authorit.
 */
@Entity
@Table(name = "authorit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Authorit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "has_role", nullable = false)
    private Role hasRole;

    @ManyToOne
    @JsonIgnoreProperties(value = "authorits", allowSetters = true)
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties(value = "authorities", allowSetters = true)
    private UserEntity user;

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

    public Authorit hasRole(Role hasRole) {
        this.hasRole = hasRole;
        return this;
    }

    public void setHasRole(Role hasRole) {
        this.hasRole = hasRole;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Authorit organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserEntity getUser() {
        return user;
    }

    public Authorit user(UserEntity userEntity) {
        this.user = userEntity;
        return this;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authorit)) {
            return false;
        }
        return id != null && id.equals(((Authorit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authorit{" +
            "id=" + getId() +
            ", hasRole='" + getHasRole() + "'" +
            "}";
    }
}
