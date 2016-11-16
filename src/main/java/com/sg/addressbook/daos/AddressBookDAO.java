/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.addressbook.daos;

import com.sg.addressbook.models.Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author apprentice
 */
public interface AddressBookDAO {
    public Address createAddress(Address address);
    public Address getAddress(int id);
    public void updateAddress(int id, Address address);
    public void deleteAddress(int id);
    public ArrayList<Address> searchByName(String name);
    public ArrayList<Address> searchByCity(String cityName);
    public ArrayList<Address> searchByState(String stateName);
    public ArrayList<Address> searchByZip(String zipCode);
    public ArrayList<Address> getAllAddresses();
    public int getAddressBookSize();
}
