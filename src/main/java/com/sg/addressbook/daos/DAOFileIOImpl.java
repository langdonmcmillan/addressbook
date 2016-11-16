/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.addressbook.daos;

import com.sg.addressbook.models.Address;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author apprentice
 */
public class DAOFileIOImpl implements AddressBookDAO {

    private ArrayList<Address> addressBook;
    private final String DELIMETER = "::";
    private final String FILE_PATH = System.getProperty("user.home") + File.separator 
            + "Documents" + File.separator + "AddressBookData";
    private final File FILE_DIR = new File(FILE_PATH);
    private final String FILE_NAME = "/addresses.txt";

    public DAOFileIOImpl() {
        addressBook = new ArrayList<>();
        if (!FILE_DIR.exists()) {
            FILE_DIR.mkdirs();
        }
        loadAddressBook();
    }

    @Override
    public Address createAddress(Address address) {
        List<Integer> allIDs = addressBook.stream().map(a -> a.getId()).collect(Collectors.toList());
        int id = (getAllAddresses().size() > 0) ? (Collections.max(allIDs) + 1) : 1;
        address.setId(id);
        addressBook.add(address);
        saveAddressBook();
        return address;
    }

    @Override
    public Address getAddress(int id) {
        return addressBook.stream().filter(a -> a.getId() == id).findFirst().get();
    }

    @Override
    public void updateAddress(int id, Address address) {
        deleteAddress(id);
        createAddress(address);
        address.setId(id);
        saveAddressBook();
    }

    @Override
    public void deleteAddress(int id) {
        Address address = addressBook.stream().filter(a -> a.getId() == id).findFirst().get();
        addressBook.remove(address);
        saveAddressBook();
    }

    @Override
    public ArrayList<Address> searchByName(String name) {
        List<Address> addressesWithName = addressBook.stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .filter(a -> a.getLastName().toLowerCase().contains(name.toLowerCase()) ||
                        a.getFirstName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return (ArrayList<Address>) addressesWithName;
    }
    
    @Override
    public ArrayList<Address> searchByCity(String cityName) {
        List<Address> addressesFromCity = addressBook.stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .filter(a -> a.getCity().toLowerCase().contains(cityName.toLowerCase()))
                .collect(Collectors.toList());
        return (ArrayList<Address>) addressesFromCity;
    }

    @Override
    public ArrayList<Address> searchByState(String stateName) {
        ArrayList<Address> addressesFromState = new ArrayList<>();
        HashMap<String, List<Address>> mapGroupedByCity = getGroupedByCity(stateName);
        List<String> cityList = mapGroupedByCity.keySet().stream().sorted().collect(Collectors.toList());
        cityList.stream().forEach(c -> addressesFromState.addAll(mapGroupedByCity.get(c)));
        return (ArrayList<Address>) addressesFromState;
    }
    
    private HashMap<String, List<Address>> getGroupedByCity(String stateName) {
        Map<String, List<Address>> mapGroupedByCity = addressBook.stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .filter(a -> a.getState().toLowerCase().contains(stateName.toLowerCase()))
                .collect(Collectors.groupingBy(Address::getCity));
        return (HashMap<String, List<Address>>) mapGroupedByCity;
    }

    @Override
    public ArrayList<Address> searchByZip(String zipCode) {
        List<Address> addressesWithZip = addressBook.stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .filter(a -> a.getZip().contains(zipCode))
                .collect(Collectors.toList());
        return (ArrayList<Address>) addressesWithZip;
    }

    @Override
    public int getAddressBookSize() {
        return addressBook.size();
    }

    @Override
    public ArrayList<Address> getAllAddresses() {
        List<Address> allAddresses = addressBook.stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .collect(Collectors.toList());
        return (ArrayList<Address>) allAddresses;
    }

    private void saveAddressBook() {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileWriter(FILE_DIR + FILE_NAME));
            for (String encodedLine : encode()) {
                output.println(encodedLine);
            }
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.out.println("Error Saving file");;
        } finally {
            output.close();
        }

    }

    private void loadAddressBook() {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_DIR + FILE_NAME)));
            addressBook = new ArrayList<>();
            while (sc.hasNextLine()) {
                String[] addressArray = sc.nextLine().split(DELIMETER);
                Address currentAddress = decode(addressArray);
                addressBook.add(currentAddress);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error loading address book.");
        } catch (IOException ex) {
            Logger.getLogger(DAOFileIOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<String> encode() {
        ArrayList<String> encodedData = new ArrayList<>();
        for (Address address : addressBook) {;
            encodedData.add("" + address.getId() + DELIMETER + address.getFirstName()
                    + DELIMETER + address.getLastName() + DELIMETER
                    + address.getStreet() + DELIMETER
                    + address.getCity() + DELIMETER + address.getState()
                    + DELIMETER + address.getZip());
        }
        return encodedData;
    }

    private Address decode(String[] addressArray) {
        Address decodedAddress = new Address();
        decodedAddress.setId(Integer.parseInt(addressArray[0]));
        decodedAddress.setFirstName(addressArray[1]);
        decodedAddress.setLastName(addressArray[2]);
        decodedAddress.setStreet(addressArray[3]);
        decodedAddress.setCity(addressArray[4]);
        decodedAddress.setState(addressArray[5]);
        decodedAddress.setZip(addressArray[6]);
        return decodedAddress;
    }
}
