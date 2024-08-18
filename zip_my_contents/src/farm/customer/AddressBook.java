package farm.customer;

import java.util.ArrayList;
import java.util.List;

/**
 * AddressBook provides functionality to store and manage a list of customers.
 * Customers can be added
 * searched by name and phone number
 * the entire list of customers can be retrieved.
 */
public class AddressBook {
    private final List<Customer> customers;

    /**
     * Constructs an empty AddressBook.
     */
    public AddressBook() {
        customers = new ArrayList<>();
    }

    /**
     * Adds a customer to the address book.
     *
     * @param customer the Customer object to be added
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Returns a list of all customer records in the address book.
     *
     * @return a List containing all Customer objects
     */
    public List<Customer> getAllRecords() {
        return customers;
    }

    /**
     * Checks if a specific customer exists in the address book.
     *
     * @param customer the Customer object to be checked
     * @return true if the customer exists in the address book, false otherwise
     */
    public boolean containsCustomer(Customer customer) {
        for (Customer c : customers) {
            if (c.equals(customer)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a customer from the address book by name and phone number.
     *
     * @param name the name of the customer
     * @param phoneNumber the phone number of the customer
     * @return the Customer object matching the name and phone number, or null if no match is found
     */
    public Customer getCustomer(String name, int phoneNumber) {
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name) && c.getPhoneNumber() == phoneNumber) {
                return c;
            }
        }
        return null;
    }

}