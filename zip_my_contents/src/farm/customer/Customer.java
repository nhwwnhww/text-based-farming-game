package farm.customer;

import java.util.Objects;

/**
 * The Customer class represents a customer with a name, phone number, and address.
 */
public class Customer {
    /**
     * Constructs a new Customer with the given name, phone number, and address.
     * param: name the name of the customer
     * param: phoneNumber the phone number of the customer
     * param: address the address of the customer
     */
    private String name;
    private int phoneNumber;
    private String address;

    /**
     * Gets the name of the customer.
     * return: the name of the customer
     */
    public Customer(String name, int phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /**
     * Gets the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name the new name of the customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return the phone number of the customer
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phoneNumber the new phone number of the customer
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the address of the customer.
     *
     * @return the address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address the new address of the customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method is a placeholder and should be implemented if the Customer class is intended to have a cart.
     *
     * @return null, as this method is not implemented
     */
    public String getCart() {
        return null;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return a string in the format "Customer{'name', phoneNumber, 'address'}"
     */
    @Override
    public String toString() {
        return "Customer{'" + name + "'," + phoneNumber + ",'" + address + "'}";
    }

    /**
     * Checks if this customer is equal to another object.
     * The customers are considered equal if they have the same name, phone number, and address.
     *
     * @param obj the object to compare with this customer
     * @return true if the given object represents a customer equivalent to this one, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Customer customer = (Customer) obj;
        return phoneNumber == customer.phoneNumber
                &&
                Objects.equals(name, customer.name)
                &&
                Objects.equals(address, customer.address);
    }

    /**
     * Returns a hash code value for the customer.
     * This is consistent with the equals method.
     *
     * @return a hash code value for this customer
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, address);
    }
}

