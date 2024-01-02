package com.estudos.data.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@JsonPropertyOrder({"key", "address", "first_name", "last_name", "gender"}) // Ordem que ficará no JSON
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    @JsonProperty("first_name") // Como ficará o nome no json.
    private String firstName;
    @JsonProperty("last_name") // Como ficará o nome no json.
    private String lastName;
    private String address;
    // @JsonIgnore // Omitindo esse atributo no json.
    private String gender;
    private Boolean enabled;

    public PersonVO() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
