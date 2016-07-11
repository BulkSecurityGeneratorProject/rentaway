package com.thoughtservice.rent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.thoughtservice.rent.domain.enumeration.GENDERFOR;

/**
 * A House.
 */
@Entity
@Table(name = "house")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "house_id")
    private String houseId;

    @NotNull
    @Column(name = "house_type", nullable = false)
    private String houseType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "house_for", nullable = false)
    private GENDERFOR houseFor;

    @NotNull
    @Column(name = "no_of_rooms", nullable = false)
    private Long noOfRooms;

    @Column(name = "food_preference")
    private String foodPreference;

    @NotNull
    @Column(name = "rent_to_be_paid", nullable = false)
    private String rentToBePaid;

    @Column(name = "rules")
    private String rules;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public GENDERFOR getHouseFor() {
        return houseFor;
    }

    public void setHouseFor(GENDERFOR houseFor) {
        this.houseFor = houseFor;
    }

    public Long getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(Long noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(String foodPreference) {
        this.foodPreference = foodPreference;
    }

    public String getRentToBePaid() {
        return rentToBePaid;
    }

    public void setRentToBePaid(String rentToBePaid) {
        this.rentToBePaid = rentToBePaid;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        House house = (House) o;
        if(house.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, house.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "House{" +
            "id=" + id +
            ", houseId='" + houseId + "'" +
            ", houseType='" + houseType + "'" +
            ", houseFor='" + houseFor + "'" +
            ", noOfRooms='" + noOfRooms + "'" +
            ", foodPreference='" + foodPreference + "'" +
            ", rentToBePaid='" + rentToBePaid + "'" +
            ", rules='" + rules + "'" +
            '}';
    }
}
