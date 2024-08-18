package farm.customer;

import farm.sales.Cart;

import java.util.Objects;

/**
 * Represents a customer in the system.
 */
public class Customer {
    private String name;
    private int phoneNumber;
    private String address;
    private Cart cart;

    /**
     * Constructs a new Customer with the specified name, phone number, and address.
     *
     * @param name        The name of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param address     The address of the customer.
     */
    public Customer(String name, int phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.cart = new Cart();  // Initialize the cart for the customer
    }

    /**
     * Returns the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name The new name of the customer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the phone number of the customer.
     *
     * @return The phone number of the customer.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phoneNumber The new phone number of the customer.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the address of the customer.
     *
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The new address of the customer.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the cart associated with this customer.
     *
     * @return The customer's cart.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return A string describing the customer.
     */
    @Override
    public String toString() {
        return "Customer{name='" + name + "', phoneNumber=" + phoneNumber + ", address='" + address;
    }

    /**
     * Compares this customer to the specified object. The result is true if and only if the argument
     * is not null and is a Customer object that has the same name, phone number, and address as this customer.
     *
     * @param o The object to compare this Customer against.
     * @return true if the given object represents a Customer equivalent to this customer, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return phoneNumber == customer.phoneNumber
                &&
                Objects.equals(name, customer.name)
                &&
                Objects.equals(address, customer.address);
    }

    /**
     * Returns a hash code value for the customer based on their name, phone number, and address.
     *
     * @return A hash code value for this customer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, address);
    }
}
