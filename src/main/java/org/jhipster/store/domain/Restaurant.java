package org.jhipster.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.jhipster.store.domain.enumeration.StatusEnum;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Customer> rescuses = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Picture> respics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Restaurant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Restaurant status(StatusEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Set<Customer> getRescuses() {
        return rescuses;
    }

    public Restaurant rescuses(Set<Customer> customers) {
        this.rescuses = customers;
        return this;
    }

    public Restaurant addRescus(Customer customer) {
        this.rescuses.add(customer);
        customer.setRestaurant(this);
        return this;
    }

    public Restaurant removeRescus(Customer customer) {
        this.rescuses.remove(customer);
        customer.setRestaurant(null);
        return this;
    }

    public void setRescuses(Set<Customer> customers) {
        this.rescuses = customers;
    }

    public Set<Picture> getRespics() {
        return respics;
    }

    public Restaurant respics(Set<Picture> pictures) {
        this.respics = pictures;
        return this;
    }

    public Restaurant addRespic(Picture picture) {
        this.respics.add(picture);
        picture.setRestaurant(this);
        return this;
    }

    public Restaurant removeRespic(Picture picture) {
        this.respics.remove(picture);
        picture.setRestaurant(null);
        return this;
    }

    public void setRespics(Set<Picture> pictures) {
        this.respics = pictures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Restaurant restaurant = (Restaurant) o;
        if (restaurant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), restaurant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
