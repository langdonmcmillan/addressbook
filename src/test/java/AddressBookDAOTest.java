/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.addressbook.daos.AddressBookDAO;
import com.sg.addressbook.daos.DAOInMemImpl;
import com.sg.addressbook.models.Address;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author apprentice
 */
public class AddressBookDAOTest {
    
    public AddressBookDAO abDAO;
    private ArrayList<Address> tempData;

    public AddressBookDAOTest() {
        abDAO = new DAOInMemImpl();
        tempData = abDAO.getAllAddresses();
        }

    @Before
    public void setUp() {
        // Assume getAllIds and deleteAddress work - start with blank data structure 
        abDAO.getAllAddresses().stream().forEach(a -> abDAO.deleteAddress(a.getId()));
    }
    
    @After
    public void reset() {
        tempData.stream().forEach(a -> abDAO.createAddress(a));
    }
    
    @Test
    public void testCreateAddressAddOne() {
        Address address = new Address();
        address.setFirstName("First");
        address.setLastName("Last");
        address.setStreet("Street");
        address.setCity("City");
        address.setState("State");
        address.setZip("12345");
        abDAO.createAddress(address);
        
        assertEquals(1, abDAO.getAddressBookSize());
        assertEquals(address, abDAO.getAddress(1));
        assertEquals("First", abDAO.getAddress(1).getFirstName());
        assertEquals("Last", abDAO.getAddress(1).getLastName());
        assertEquals("Street", abDAO.getAddress(1).getStreet());
        assertEquals("City", abDAO.getAddress(1).getCity());
        assertEquals("State", abDAO.getAddress(1).getState());
        assertEquals("12345", abDAO.getAddress(1).getZip());
    }
    
    @Test
    public void testCreateAddressAddTen() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        assertEquals(10, abDAO.getAddressBookSize());
        assertEquals(10, abDAO.getAddress(10).getId());
        assertEquals("First10", abDAO.getAddress(10).getFirstName());
        assertEquals("Last10", abDAO.getAddress(10).getLastName());
        assertEquals("Street10", abDAO.getAddress(10).getStreet());
        assertEquals("City10", abDAO.getAddress(10).getCity());
        assertEquals("State10", abDAO.getAddress(10).getState());
        assertEquals("10", abDAO.getAddress(10).getZip());
    }
    
    @Test
    public void testCreateAddressAdd525() {
        for (int i = 1; i <= 525; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        assertEquals(525, abDAO.getAddressBookSize());
        assertEquals(525, abDAO.getAddress(525).getId());
        assertEquals("First525", abDAO.getAddress(525).getFirstName());
        assertEquals("Last525", abDAO.getAddress(525).getLastName());
        assertEquals("Street525", abDAO.getAddress(525).getStreet());
        assertEquals("City525", abDAO.getAddress(525).getCity());
        assertEquals("State525", abDAO.getAddress(525).getState());
        assertEquals("525", abDAO.getAddress(525).getZip());
    }
    
    @Test
    public void testDelete() {
        for (int i = 1; i <= 525; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
        abDAO.deleteAddress(300);
        assertEquals(524, abDAO.getAddressBookSize());
        
        abDAO.deleteAddress(1);
        abDAO.deleteAddress(2);
        abDAO.deleteAddress(3);
        abDAO.deleteAddress(4);
        assertEquals(520, abDAO.getAddressBookSize());   
                
        assertEquals(525, abDAO.getAddress(525).getId());
        assertEquals("First525", abDAO.getAddress(525).getFirstName());
        
        Address address = new Address();
        address.setFirstName("Firstafter");
        address.setLastName("Lastafter");
        address.setStreet("Streetafter");
        address.setCity("Cityafter");
        address.setState("Stateafter");
        address.setZip("" + 55555);
        abDAO.createAddress(address);
        
        assertEquals(address, abDAO.getAddress(526));
        assertEquals(521, abDAO.getAddressBookSize()); 
    }
    
    @Test
    public void testUpdateAddress() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
        Address updatedAddress = new Address();
        updatedAddress.setFirstName("FirstUpdate");
        updatedAddress.setLastName("LastUpdate");
        updatedAddress.setStreet("StreetUpdate");
        updatedAddress.setCity("CityUpdate");
        updatedAddress.setState("StateUpdate");
        updatedAddress.setZip("" + 55555);
        
        abDAO.updateAddress(5, updatedAddress);
        
        assertEquals(updatedAddress, abDAO.getAddress(5));
        assertEquals(10, abDAO.getAddressBookSize()); 
    }
    
    @Test
    public void testSearchByFullName() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
      //  assertEquals(1, abDAO.searchByName("Last1").size());
      //  int id = abDAO.searchByName("Last1").get(0);
     //   assertEquals(1, id);
      //  assertEquals(1, abDAO.searchByName("First5", "Last5").size());
      //  id = abDAO.searchByName("First5", "Last5").get(0);
     //   assertEquals(5, id);
        
        Address address = new Address();
        address.setFirstName("First5");
        address.setLastName("Last5");
        address.setStreet("Street5");
        address.setCity("City5");
        address.setState("State5");
        address.setZip("" + 12345);
        abDAO.createAddress(address);
        
       // assertEquals(2, abDAO.searchByName("First5", "Last5").size());
       // assertTrue(abDAO.searchByName("First5", "Last5").contains(5));
        //assertTrue(abDAO.searchByName("First5", "Last5").contains(11));
    }
    
    @Test
    public void testSearches() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
        assertEquals(10, abDAO.searchByName("Last").size());
        assertEquals(1, abDAO.searchByName("Last3").size());
        assertEquals(1, abDAO.searchByName("last5").size());
        assertEquals(0, abDAO.searchByName("Laawegfstafew1").size());
        
        assertEquals(10, abDAO.searchByCity("City").size());
        assertEquals(1, abDAO.searchByCity("City7").size());
        assertEquals(1, abDAO.searchByCity("city3").size());
        assertEquals(0, abDAO.searchByCity("awofgih").size());
        
        assertEquals(10, abDAO.searchByState("State").size());
        assertEquals(1, abDAO.searchByState("State3").size());
        assertEquals(1, abDAO.searchByState("state9").size());
        assertEquals(0, abDAO.searchByState("wfeft").size());
        
        assertEquals(10, abDAO.searchByZip("").size());
        assertEquals(2, abDAO.searchByZip("1").size());
        assertEquals(1, abDAO.searchByZip("2").size());
        assertEquals(0, abDAO.searchByZip("99").size());
    }
    
    @Test
    public void testGetAllAddresses() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
        assertEquals(10, abDAO.getAllAddresses().size());
        
    }
    
    @Test
    public void testGetAddressBookSize() {
        for (int i = 1; i <= 10; i++) {
            Address address = new Address();
            address.setFirstName("First" + i);
            address.setLastName("Last" + i);
            address.setStreet("Street" + i);
            address.setCity("City" + i);
            address.setState("State" + i);
            address.setZip("" + i);
            abDAO.createAddress(address);
        }
        
        assertEquals(10, abDAO.getAddressBookSize());
    }
}
