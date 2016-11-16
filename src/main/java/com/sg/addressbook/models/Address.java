/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.addressbook.models;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author apprentice
 */
public class Address {
    private int id;
    @Length(max = 50, message = "Cannot be greater than 50 characters")
    @NotEmpty(message = "Please enter a first name.")
    private String firstName;
    @Length(max = 50, message = "Cannot be greater than 50 characters")
    @NotEmpty(message = "Please enter a last name.")
    private String lastName;
    @Length(max = 50, message = "Cannot be greater than 50 characters")
    @NotEmpty(message = "Please enter a street.")
    private String street;
    @Length(max = 50, message = "Cannot be greater than 50 characters")
    @NotEmpty(message = "Please enter a city.")
    private String city;
    @Length(max = 50, message = "Cannot be greater than 50 characters")
    @NotEmpty(message = "Please enter a state.")
    private String state;
    @Length(max = 5, min = 5, message = "Please enter a valid zip code")
    @Pattern(regexp = "^(0|[1-9][0-9]*)$", message = "The zip code must be only numbers")
    @NotEmpty(message = "Please enter a zip code.")
    private String zip;
    
    public Address() {
        
    }

    public int getId() {
        return id;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public String getName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return getName() + ": " + city + ", " + state;
    }
    
    public String getFullAddress() {
        return getName() + "\n" + street + "\n" + city + ", " + state + " " + zip + "\n";
    }

    public void setId(int id) {
        this.id = id;
    }
}
