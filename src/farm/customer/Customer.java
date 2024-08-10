package farm.customer;

public class Customer{
    private String name;
    private int phoneNumber;
    private String address;

    public Customer(String name, int phoneNumber, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCart(){
        return null;
    }

    public String toString(){
        return "Customer{'"+name+"',"+phoneNumber+",'"+address+"'}";
    }

    public boolean equals(Object){

    }

    public int hashCode(){

    }
}

public class AddressBook{
    private List<Customer> = customers;

    public void addCustomer(Customer){

    }

    public List<Customer> getAllRecords(){

    }

    public boolean containsCustomer(Customer){

    }

    public Customer getCustomer(String name, int phoneNumber){

    }

}