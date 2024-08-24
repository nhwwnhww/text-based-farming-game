package farm.customer;

import farm.core.CustomerNotFoundException;
import farm.core.DuplicateCustomerException;

import java.util.ArrayList;
import java.util.List;

/**
 * AddressBook provides functionality to store and manage a list of customers.
 * Customers can be added
 * searched by name and phone number
 * the entire list of customers can be retrieved
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
     * If a customer with the same name and phone number already exists, a DuplicateCustomerException is thrown.
     *
     * @param customer The customer to be added.
     * @throws DuplicateCustomerException if a customer with the same name and phone number already exists.
     */
    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        if (containsCustomer(customer)) {
            throw new DuplicateCustomerException("Customer already exists: " + customer.getName());
        }
        customers.add(customer);
    }

    /**
     * Returns a list of all customers in the address book
     *
     * @return a List containing all Customer objects
     */
    public List<Customer> getAllRecords() {
        return new ArrayList<>(customers);
    }

    /**
     * Checks if a customer with the same name and phone number already exists in the address book
     *
     * @param customer The customer to check
     * @return true if the customer exists, false otherwise
     */
    public boolean containsCustomer(Customer customer) {
        return customers.contains(customer);
    }

    /**
     * Retrieves a customer by their name and phone number
     * If no such customer exists, a CustomerNotFoundException is thrown
     *
     * @param name The name of the customer
     * @param phoneNumber The phone number of the customer
     * @return The customer with the specified name and phone number
     * @throws CustomerNotFoundException if no customer with the specified name and phone number exists
     */
    public Customer getCustomer(String name, int phoneNumber) throws CustomerNotFoundException {
        for (Customer customer : customers) {
            if (customer.getName().equals(name) && customer.getPhoneNumber() == phoneNumber) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found: " + name);
    }

}