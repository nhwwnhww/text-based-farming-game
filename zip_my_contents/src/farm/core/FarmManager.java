package farm.core;

import farm.customer.Customer;
import farm.sales.*;
import farm.core.ShopFront;
import farm.sales.transaction.CategorisedTransaction;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

/**
 * Controller class, coordinating information between the model and view/UI of the program.
 * <p>
 * The FarmManager class is responsible for managing the farm's inventory, creating customers, and initiating transactions.
 * It interacts with the ShopFront to display messages and prompt user inputs.
 * </p>
 * <p>
 * The FarmManager class should be moved to the 'src/farm/core' directory to compile properly with the provided project structure.
 * </p>
 * <p>
 * The class has been implemented in Stage 2, and additional functionalities related to quantities will be introduced in Stage 3.
 * </p>
 */
public class FarmManager extends Object {

    private final Farm farm;
    private final ShopFront shop;
    private final boolean enableFancy;

    /**
     * Constructs a new FarmManager instance with a farm and shop provided.
     *
     * @param farm the model for the program.
     * @param shop the UI/view for the program.
     */
    public FarmManager(Farm farm, ShopFront shop) {
        this.farm = farm;
        this.enableFancy = false;
        this.shop = new ShopFront();
    }

    /**
     * Constructs a new FarmManager instance with a farm and shop provided,
     * and the ability to enable the inventory type.
     *
     * @param farm        the model for the program.
     * @param shop        the UI/view for the program.
     * @param enableFancy flag indicating whether to use the FancyInventory inventory type.
     */
    public FarmManager(Farm farm, ShopFront shop, boolean enableFancy) {
        this.farm = farm;
        this.shop = shop;
        this.enableFancy = true;
    }

    /**
     * Begins the running of the UI and interprets user input to begin the appropriate mode.
     */
    public void run() {
        // Implementation provided
    }

    /**
     * Adds a single product with the corresponding name to the Farm's inventory.
     * <p>
     * Displays a success message if the product is added successfully. Displays a failure message if the product name is invalid or an exception is thrown.
     * </p>
     *
     * @param productName the name of the product to add to the farm.
     */
    protected void addToInventory(String productName) {
        try {
            if ("egg".equals(productName)
                    || "milk".equals(productName)
                    || "jam".equals(productName)
                    || "wool".equals(productName)) {
                farm.addProduct(productName, 1); // Add 1 item of the product
                shop.displayProductAddSuccess();
            } else {
                shop.displayProductAddFailed("Invalid product name: " + productName);
            }
        } catch (Exception e) {
            shop.displayProductAddFailed("Error adding product: " + e.getMessage());
        }
    }

    /**
     * Adds a certain number of the given product with the corresponding name to the Farm's inventory.
     * <p>
     * Displays a success message if the product is added successfully. Displays a failure message if the product name is invalid or an exception is thrown.
     * </p>
     *
     * @param productName the name of the product to add to the farm.
     * @param quantity    the amount of the product to add.
     */
    protected void addToInventory(String productName, int quantity) {
        try {
            if ("egg".equals(productName)
                    || "milk".equals(productName)
                    || "jam".equals(productName)
                    || "wool".equals(productName)) {
                farm.addProduct(productName, quantity);
                shop.displayProductAddSuccess();
            } else {
                shop.displayProductAddFailed("Invalid product name: " + productName);
            }
        } catch (Exception e) {
            shop.displayProductAddFailed("Error adding product: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to create a new customer and then saves it to the farm's address book for later usage.
     * <p>
     * Displays messages for duplicate customers and invalid phone numbers as needed.
     * </p>
     */
    protected void createCustomer() throws CustomerNotFoundException, DuplicateCustomerException {
        String name = shop.promptForCustomerName();
        String phoneNumber = String.valueOf(shop.promptForCustomerNumber());
        String address = shop.promptForCustomerAddress();

        // Check for duplicate customer
        if (farm.getCustomer(name, phoneNumber) != null) {
            shop.displayDuplicateCustomer();
            return;
        }

        // Validate phone number
        if (!phoneNumber.matches("\\d+")) {
            shop.displayInvalidPhoneNumber();
            return;
        }

        // Create and save the new customer
        Customer customer = new Customer(name, Integer.parseInt(phoneNumber), address);
        farm.addCustomer(customer);

    }

    /**
     * Starts a new transaction for the transaction manager to manage.
     * <p>
     * Prompts the user for customer details and creates the appropriate type of transaction.
     * Displays messages for invalid phone numbers, customer not found, and failed transaction creation.
     * </p>
     *
     * @param transactionType the type of transaction to make.
     */
    protected void initiateTransaction(String transactionType) {
        try {
            String customerName = shop.promptForCustomerName();
            String phoneNumber = String.valueOf(shop.promptForCustomerNumber());

            // Validate phone number
            if (!phoneNumber.matches("\\d+")) {
                shop.displayInvalidPhoneNumber();
                return;
            }

            // Look up customer
            Customer customer = farm.getCustomer(customerName, phoneNumber);
            if (customer == null) {
                shop.displayCustomerNotFound();
                return;
            }

            // Create the transaction
            Transaction transaction = switch (transactionType.toLowerCase()) {
                case "-s", "-specialise" -> new SpecialSaleTransaction(customer);
                case "-c", "-categorised" -> new CategorisedTransaction(customer);
                default -> new Transaction(customer);
            };

            // Prompt for discounts if needed
            if (transaction instanceof SpecialSaleTransaction) {
                // Get discounts logic for SpecialSaleTransaction
                transaction = new SpecialSaleTransaction(customer);
            }

            shop.displayTransactionStart();
            // Start the transaction
            // Logic to start the transaction

        } catch (Exception e) {
            shop.displayFailedToCreateTransaction();
        }
    }
}
