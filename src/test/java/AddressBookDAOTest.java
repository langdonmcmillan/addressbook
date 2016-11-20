/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.addressbook.daos.AddressBookDAO;
import com.sg.addressbook.daos.DAOInMemImpl;
import com.sg.addressbook.models.Address;
import com.sg.addressbook.models.City;
import com.sg.addressbook.models.State;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author apprentice
 */
public class AddressBookDAOTest {
    
    public AddressBookDAO abDAO;

    @Before
    public void setUp() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        abDAO = ctx.getBean("addressBookDB", AddressBookDAO.class);
        
        JdbcTemplate template = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        
        template.execute("delete from Addresses");
        template.execute("delete from Cities");
    }
    
    @Test
    public void testCreateAddressAddOne() {
        City city = new City();
        State state = new State();
        Address address = new Address();
        
        city.setCityName("CityName");
        city.setStateID(1);
        state.setStateID(1);
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity(city);
        address.setState(state);
        address.setZip("12345");
        abDAO.createAddress(address);
        
        assertEquals(1, abDAO.getAddressBookSize());
        assertEquals("First", abDAO.getAddress(address.getId()).getFirstName());
        assertEquals("Last", abDAO.getAddress(address.getId()).getLastName());
        assertEquals("Street", abDAO.getAddress(address.getId()).getStreet());
        assertEquals("CityName", abDAO.getAddress(address.getId()).getCity().getCityName());
        assertEquals("Alabama", abDAO.getAddress(address.getId()).getState().getStateName());
        assertEquals("12345", abDAO.getAddress(address.getId()).getZip());
    }
    
    @Test
    public void testCreateAddressAddTen() {
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("CityName");
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
        }
        
        City city = new City();
        State state = new State();
        Address address = new Address();
        
        city.setCityName("CityName");
        city.setStateID(1);
        state.setStateID(1);
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity(city);
        address.setState(state);
        address.setZip("12345");
        abDAO.createAddress(address);
        
        assertEquals(11, abDAO.getAddressBookSize());
        
    }
    
    
    
    @Test
    public void testDelete() {
        int addressToDelete = 0;
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("CityName");
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
            addressToDelete = address.getId();
        }
        
        abDAO.deleteAddress(addressToDelete);
        assertEquals(9, abDAO.getAddressBookSize());
        
        abDAO.deleteAddress(addressToDelete - 1);
        abDAO.deleteAddress(addressToDelete - 2);
        assertEquals(7, abDAO.getAddressBookSize());   

    }
    
    @Test
    public void testUpdateAddress() {
        City city = new City();
        State state = new State();
        Address address = new Address();
        
        city.setCityName("CityName");
        city.setStateID(1);
        state.setStateID(1);
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity(city);
        address.setState(state);
        address.setZip("12345");
        abDAO.createAddress(address);
        
        assertEquals(1, abDAO.getAddressBookSize());
        assertEquals("First", abDAO.getAddress(address.getId()).getFirstName());
        assertEquals("Last", abDAO.getAddress(address.getId()).getLastName());
        assertEquals("Street", abDAO.getAddress(address.getId()).getStreet());
        assertEquals("CityName", abDAO.getAddress(address.getId()).getCity().getCityName());
        assertEquals("Alabama", abDAO.getAddress(address.getId()).getState().getStateName());
        assertEquals("12345", abDAO.getAddress(address.getId()).getZip());
        
        int addressID = address.getId();
        city = new City();
        state = new State();
        address = new Address();
        address = abDAO.getAddress(addressID);
        
        city.setCityName("UpdatedCityName");
        city.setStateID(2);
        state.setStateID(2);
        address.setFirstName("UpdatedFirst");
        address.setLastName("UpdatedLast");
        address.setStreet("UpdatedStreet");
        address.setCity(city);
        address.setState(state);
        address.setZip("54321");
        abDAO.updateAddress(address);
        
        assertEquals(1, abDAO.getAddressBookSize());
        assertEquals("UpdatedFirst", abDAO.getAddress(address.getId()).getFirstName());
        assertEquals("UpdatedLast", abDAO.getAddress(address.getId()).getLastName());
        assertEquals("UpdatedStreet", abDAO.getAddress(address.getId()).getStreet());
        assertEquals("UpdatedCityName", abDAO.getAddress(address.getId()).getCity().getCityName());
        assertEquals("Alaska", abDAO.getAddress(address.getId()).getState().getStateName());
        assertEquals("54321", abDAO.getAddress(address.getId()).getZip());
    }
    
    @Test
    public void testSearchByFullName() {
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("CityName");
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
        }
        
        assertEquals(10, abDAO.searchByName("").size());
        assertEquals(10, abDAO.searchByName("first").size());
        assertEquals(10, abDAO.searchByName("last").size());
        assertEquals(10, abDAO.searchByName("first last").size());
        assertEquals(10, abDAO.searchByName("fi la").size());
        assertEquals(10, abDAO.searchByName("f l").size());
        assertEquals(2, abDAO.searchByName("first1").size());
        assertEquals(1, abDAO.searchByName("f last3").size());
        assertEquals(0, abDAO.searchByName("firstlast").size());
    }
    
    @Test
    public void testSearchByCity() {
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("City Name" + i);
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
        }
        
        assertEquals(10, abDAO.searchByCity("").size());
        assertEquals(10, abDAO.searchByCity("City Name").size());
        assertEquals(10, abDAO.searchByCity("city").size());
        assertEquals(10, abDAO.searchByCity("y name").size());
        assertEquals(2, abDAO.searchByCity("City Name1").size());
        assertEquals(1, abDAO.searchByCity("Name2").size());
        assertEquals(0, abDAO.searchByCity("cityname").size());
    }

    @Test
    public void testSearchByState() {
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("CityName");
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
        }
        
        City city = new City();
        State state = new State();
        Address address = new Address();
        
        city.setCityName("CityName");
        city.setStateID(1);
        state.setStateID(1);
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity(city);
        address.setState(state);
        address.setZip("12345");
        abDAO.createAddress(address);
        
        assertEquals(11, abDAO.searchByState("").size());
        assertEquals(2, abDAO.searchByState("alabama").size());
        assertEquals(4, abDAO.searchByState("al").size());
        assertEquals(1, abDAO.searchByState("alaska").size());
        assertEquals(1, abDAO.searchByState("ak").size());
        assertEquals(1, abDAO.searchByState("Cal").size());
        assertEquals(1, abDAO.searchByState("CA").size());
        assertEquals(0, abDAO.searchByState("carolina").size());
    }
    
    @Test
    public void testSearchByZip() {
        for (int i = 1; i <= 10; i++) {
            City city = new City();
            State state = new State();
            Address address = new Address();
            
            city.setCityName("CityName");
            city.setStateID(i);
            state.setStateID(i);
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity(city);
            address.setState(state);
            address.setZip("12345");
            abDAO.createAddress(address);
        }
        
        City city = new City();
        State state = new State();
        Address address = new Address();
        
        city.setCityName("CityName");
        city.setStateID(1);
        state.setStateID(1);
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity(city);
        address.setState(state);
        address.setZip("54321");
        abDAO.createAddress(address);
        
        assertEquals(11, abDAO.searchByZip("").size());
        assertEquals(11, abDAO.searchByZip("5").size());
        assertEquals(10, abDAO.searchByZip("12345").size());
        assertEquals(1, abDAO.searchByZip("54321").size());
    }
}
