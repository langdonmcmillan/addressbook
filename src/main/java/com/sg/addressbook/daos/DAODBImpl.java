/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.addressbook.daos;

import com.sg.addressbook.models.Address;
import com.sg.addressbook.models.City;
import com.sg.addressbook.models.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author apprentice
 */
public class DAODBImpl implements AddressBookDAO {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    private static final String INSERT_ADDRESS = "insert into Addresses (firstName, lastName, street, cityID, stateID, zip) "
            + "values (:firstName, :lastName, :street, :cityID, :stateID, :zip)";
    private static final String UPDATE_ADDRESS = "update Addresses set firstName = :firstName, lastName = :lastName, street = :street,"
            + "cityID = :cityID, stateID = :stateID, zip = :zip where addressID = :addressID";
    private static final String INSERT_CITY = "insert ignore into Cities (cityName, stateID) values (:cityName, :stateID)";
    private static final String GET_ADDRESS = "select addressID, firstName, lastName, street, cityID, stateID, zip from Addresses where addressID = ?";
    private static final String GET_CITY = "select c.cityID, c.cityName, c.stateID from Cities c "
            + "join Addresses a on a.cityID = c.cityID where a.addressID = ?";
    private static final String GET_CITY_ID = "select cityID from Cities "
            + "where cityName = :cityName and stateID = :stateID";
    private static final String GET_STATE = "select s.stateID, s.stateName, s.stateInitials from States s "
            + "join Addresses a on a.stateID = s.stateID where a.addressID = ?";
    private static final String GET_ALL_ADDRESSES = "select addressID, firstName, lastName,"
            + " street, cityID, stateID, zip from Addresses order by lastName asc, firstName asc, stateID asc, cityID";
    private static final String DELETE_ADDRESS = "delete from Addresses where addressID = ?";
    
    private static final String SEARCH_BY_NAME = "select distinct addressID, firstName, lastName, street, "
            + "a.cityID, a.stateID, zip from Addresses a "
            + "join Cities c on a.cityID = c.cityID "
            + "where concat_ws(' ', firstName, lastName) like ? "
            + "order by lastName asc, firstName asc, a.stateID asc, c.cityName";
    private static final String SEARCH_BY_CITY = "select distinct addressID, firstName, lastName, street, "
            + "a.cityID, a.stateID, zip from Addresses a "
            + "join Cities c on a.cityID = c.cityID "
            + "where c.cityName like ? "
            + "group by a.stateID, addressID, firstName, lastName, street, a.cityID, zip "
            + "order by lastName asc, firstName asc, a.stateID asc, c.cityName asc";
    private static final String SEARCH_BY_STATE = "select distinct addressID, firstName, lastName, street, "
            + "a.cityID, a.stateID, zip from Addresses a "
            + "join States s on a.stateID = s.stateID "
            + "join Cities c on a.cityID = c.cityID "
            + "where stateName like ? or stateInitials = ? "
            + "group by a.stateID, addressID, firstName, lastName, street, a.cityID, zip "
            + "order by lastName asc, firstName asc, a.stateID asc, c.cityName asc";
    private static final String SEARCH_BY_ZIP = "select distinct addressID, firstName, lastName, street, "
            + "a.cityID, a.stateID, zip from Addresses a "
            + "join Cities c on a.cityID = c.cityID "
            + "where zip like ? "
            + "group by a.stateID, addressID, firstName, lastName, street, a.cityID, zip "
            + "order by lastName asc, firstName asc, a.stateID asc, c.cityName asc";
    
    private static final String GET_ALL_STATES = "select stateID, stateName, stateInitials from States";
    private static final String GET_STATE_ID = "select stateID from States where stateName = ?";
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Address createAddress(Address address) {
        City city = insertCity(address.getCity());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("firstName", address.getFirstName());
        parameters.addValue("lastName", address.getLastName());
        parameters.addValue("street", address.getStreet());
        parameters.addValue("cityID", city.getCityID());
        parameters.addValue("stateID", address.getState().getStateID());
        parameters.addValue("zip", address.getZip());
        namedParameterJdbcTemplate.update(INSERT_ADDRESS, parameters);
        address.setId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class));
        return address;
    }

    private City insertCity(City city) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cityName", city.getCityName());
        parameters.addValue("stateID", city.getStateID());
        namedParameterJdbcTemplate.update(INSERT_CITY, parameters);
        city.setCityID(namedParameterJdbcTemplate.queryForObject(GET_CITY_ID, parameters, Integer.class));
        return city;
    }
    
    @Override
    public Integer getStateID(String stateName) {
        Integer stateID = jdbcTemplate.queryForObject(GET_STATE_ID, Integer.class, stateName);
        return stateID;
    }

    @Override
    public Address getAddress(int id) {
        try {
            City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), id);
            State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), id);
            Address address = jdbcTemplate.queryForObject(GET_ADDRESS, new AddressMapper(), id);
            address.setCity(city);
            address.setState(state);
            return address;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Address> searchByName(String name) {
        String searchTerm = "%";
        String[] searchArray = name.split(" ");
        for (String s: searchArray) {
            searchTerm += s + "%";
        }
        try {
            List<Address> allAddresses = jdbcTemplate.query(SEARCH_BY_NAME, new AddressMapper(), searchTerm);
            for (Address address : allAddresses) {
                City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), address.getId());
                State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), address.getId());
                address.setCity(city);
                address.setState(state);
            }
            return (ArrayList<Address>) allAddresses;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Address> searchByCity(String cityName) {
        String searchTerm = "%";
        String[] searchArray = cityName.split(" ");
        for (String s: searchArray) {
            searchTerm += s + "%";
        }
        try {
            List<Address> allAddresses = jdbcTemplate.query(SEARCH_BY_CITY, new AddressMapper(), searchTerm);
            for (Address address : allAddresses) {
                City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), address.getId());
                State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), address.getId());
                address.setCity(city);
                address.setState(state);
            }
            return (ArrayList<Address>) allAddresses;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Address> searchByState(String stateName) {
        String searchTerm = "%" + stateName + "%";
        try {
            List<Address> allAddresses = jdbcTemplate.query(SEARCH_BY_STATE, new AddressMapper(), searchTerm, stateName);
            for (Address address : allAddresses) {
                City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), address.getId());
                State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), address.getId());
                address.setCity(city);
                address.setState(state);
            }
            return (ArrayList<Address>) allAddresses;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Address> searchByZip(String zipCode) {
        String searchTerm = "%" + zipCode + "%";
        try {
            List<Address> allAddresses = jdbcTemplate.query(SEARCH_BY_ZIP, new AddressMapper(), searchTerm);
            for (Address address : allAddresses) {
                City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), address.getId());
                State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), address.getId());
                address.setCity(city);
                address.setState(state);
            }
            return (ArrayList<Address>) allAddresses;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Address> getAllAddresses() {
        try {
            List<Address> allAddresses = jdbcTemplate.query(GET_ALL_ADDRESSES, new AddressMapper());
            for (Address address : allAddresses) {
                City city = jdbcTemplate.queryForObject(GET_CITY, new CityMapper(), address.getId());
                State state = jdbcTemplate.queryForObject(GET_STATE, new StateMapper(), address.getId());
                address.setCity(city);
                address.setState(state);
            }
            return (ArrayList<Address>) allAddresses;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public int getAddressBookSize() {
        return getAllAddresses().size();
    }

    @Override
    public ArrayList<State> getAllStates() {
        try {
            List<State> allStates = jdbcTemplate.query(GET_ALL_STATES, new StateMapper());
            return (ArrayList<State>) allStates;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static final class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int i) throws SQLException {
            Address address = new Address();
            address.setId(rs.getInt("addressID"));
            address.setFirstName(rs.getString("firstName"));
            address.setLastName(rs.getString("lastName"));
            address.setStreet(rs.getString("street"));
            address.setZip(rs.getString("zip"));
            return address;
        }
    }

    private static final class CityMapper implements RowMapper<City> {

        @Override
        public City mapRow(ResultSet rs, int i) throws SQLException {
            City city = new City();
            city.setCityID(rs.getInt("cityID"));
            city.setCityName(rs.getString("cityName"));
            city.setStateID(rs.getInt("stateID"));
            return city;
        }
    }

    private static final class StateMapper implements RowMapper<State> {

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {
            State state = new State();
            state.setStateID(rs.getInt("stateID"));
            state.setStateName(rs.getString("stateName"));
            state.setStateInitials(rs.getString("stateInitials"));
            return state;
        }
    }

    @Override
    public void updateAddress(Address address) {
        City city = insertCity(address.getCity());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("addressID", address.getId());
        parameters.addValue("firstName", address.getFirstName());
        parameters.addValue("lastName", address.getLastName());
        parameters.addValue("street", address.getStreet());
        parameters.addValue("cityID", city.getCityID());
        parameters.addValue("stateID", address.getState().getStateID());
        parameters.addValue("zip", address.getZip());
        namedParameterJdbcTemplate.update(UPDATE_ADDRESS, parameters);
    }

    @Override
    public void deleteAddress(int id) {
        jdbcTemplate.update(DELETE_ADDRESS, id);
    }

}
