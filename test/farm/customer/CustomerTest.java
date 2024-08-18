package farm.customer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerTest {
    private Customer customer;

    @Before
    public void setUp() {
        this.customer = new Customer( "Ali",33651111, "UQ");
    }

    @Test
    public void getNameTest() {
        assertEquals("Incorrect name", "Ali", customer.getName());
    }

    @Test
    public void setNameTest() {
        customer.setName("Alexandra");
        assertEquals("Name not updated correctly", "Alexandra", customer.getName());
    }
    
    @Test 
    public void getAddressTest() {
        assertEquals("Incorrect address", "UQ", customer.getAddress());
    }

    @Test
    public void setAddressTest() {
        customer.setAddress("Brisbane");
        assertEquals("Address not updated correctly", "Brisbane", customer.getAddress());
    }

    @Test
    public void getPhoneNumberTest() {
        assertEquals("Incorrect phone number", 33651111, customer.getPhoneNumber());
    }

    @Test
    public void setPhoneNumberTest() {
        customer.setPhoneNumber(12345678);
        assertEquals("Phone number not updated correctly", 12345678, customer.getPhoneNumber());
    }


    @Test
    public void toStringTest() {
        assertEquals("Incorrect toString method. Is the format correct?", "Name: Ali | Phone Number: 33651111 | Address: UQ", customer.toString());
    }


    @After
    public void tearDown() {
        this.customer = null;
    }
}