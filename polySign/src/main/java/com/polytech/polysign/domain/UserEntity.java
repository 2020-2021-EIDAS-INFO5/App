package com.polytech.polysign.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserEntity.
 */
@Entity
@Table(name = "user_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 15)
    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SignatureProcess> signatures = new HashSet<>();

    @OneToMany(mappedBy = "signer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SignOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Authorit> authorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserEntity firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public UserEntity lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public UserEntity phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public UserEntity user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<SignatureProcess> getSignatures() {
        return signatures;
    }

    public UserEntity signatures(Set<SignatureProcess> signatureProcesses) {
        this.signatures = signatureProcesses;
        return this;
    }

    public UserEntity addSignature(SignatureProcess signatureProcess) {
        this.signatures.add(signatureProcess);
        signatureProcess.setCreator(this);
        return this;
    }

    public UserEntity removeSignature(SignatureProcess signatureProcess) {
        this.signatures.remove(signatureProcess);
        signatureProcess.setCreator(null);
        return this;
    }

    public void setSignatures(Set<SignatureProcess> signatureProcesses) {
        this.signatures = signatureProcesses;
    }

    public Set<SignOrder> getOrders() {
        return orders;
    }

    public UserEntity orders(Set<SignOrder> signOrders) {
        this.orders = signOrders;
        return this;
    }

    public UserEntity addOrder(SignOrder signOrder) {
        this.orders.add(signOrder);
        signOrder.setSigner(this);
        return this;
    }

    public UserEntity removeOrder(SignOrder signOrder) {
        this.orders.remove(signOrder);
        signOrder.setSigner(null);
        return this;
    }

    public void setOrders(Set<SignOrder> signOrders) {
        this.orders = signOrders;
    }

    public Set<Authorit> getAuthorities() {
        return authorities;
    }

    public UserEntity authorities(Set<Authorit> authorits) {
        this.authorities = authorits;
        return this;
    }

    public UserEntity addAuthority(Authorit authorit) {
        this.authorities.add(authorit);
        authorit.setUser(this);
        return this;
    }

    public UserEntity removeAuthority(Authorit authorit) {
        this.authorities.remove(authorit);
        authorit.setUser(null);
        return this;
    }

    public void setAuthorities(Set<Authorit> authorits) {
        this.authorities = authorits;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        return id != null && id.equals(((UserEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserEntity{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
