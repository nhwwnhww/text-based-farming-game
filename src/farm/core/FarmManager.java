package farm.core;

import farm.customer.Customer;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.sales.*;
import farm.core.ShopFront;
import farm.sales.transaction.CategorisedTransaction;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

import java.util.List;
import java.util.Map;

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
public class FarmManager {

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
        System.out.println("Welcome to the Farm Management System!");
        boolean running = true;

        while (running) {
            List<String> command = shop.promptModeSelect();

            switch (command.getFirst()) {
                case "q":
                    running = false;
                    break;

                case "inventory":
                    handleInventoryMode(shop);
                    break;

                case "address":
                    handleAddressBookMode(shop);
                    break;

                case "sales":
                    handleSalesMode(shop);
                    break;

                case "history":
                    handleHistoryMode(shop);
                    break;

                default:
                    shop.displayIncorrectArguments();
                    break;
            }
        }

        System.out.println("Thank you for using the Farm Program!");
    }

    private void handleInventoryMode(ShopFront shopFront) {
        boolean inInventoryMode = true;

        while (inInventoryMode) {
            List<String> command = shopFront.promptInventoryCmd();

            switch (command.getFirst()) {
                case "q":
                    inInventoryMode = false;
                    break;

                case "add":
                    // Handle adding a product to the inventory
                    break;

                case "list":
                    // Handle listing all products in the inventory
                    break;

                default:
                    shopFront.displayIncorrectArguments();
                    break;
            }
        }
    }

    private void handleAddressBookMode(ShopFront shopFront) {
        boolean inAddressBookMode = true;

        while (inAddressBookMode) {
            List<String> command = shopFront.promptAddressBookCmd();

            switch (command.getFirst()) {
                case "q":
                    inAddressBookMode = false;
                    break;

                case "add":
                    // Handle adding a customer to the address book
                    break;

                case "list":
                    // Handle listing all customers in the address book
                    break;

                default:
                    shopFront.displayIncorrectArguments();
                    break;
            }
        }
    }

    private void handleSalesMode(ShopFront shopFront) {
        boolean inSalesMode = true;

        while (inSalesMode) {
            List<String> command = shopFront.promptSalesCmd();

            switch (command.getFirst()) {
                case "q":
                    inSalesMode = false;
                    break;

                case "start":
                    // Handle starting a new transaction
                    break;

                case "add":
                    // Handle adding a product to the current transaction
                    break;

                case "checkout":
                    // Handle checking out the current transaction
                    break;

                default:
                    shopFront.displayIncorrectArguments();
                    break;
            }
        }
    }

    private void handleHistoryMode(ShopFront shopFront) {
        boolean inHistoryMode = true;

        while (inHistoryMode) {
            List<String> command = shopFront.promptHistoryCmd();

            switch (command.get(0)) {
                case "q":
                    inHistoryMode = false;
                    break;

                case "stats":
                    // Handle displaying statistics
                    break;

                case "last":
                    // Handle displaying the last transaction's receipt
                    break;

                case "grossing":
                    // Handle displaying the highest-grossing transaction's receipt
                    break;

                case "popular":
                    // Handle displaying the most popular product
                    break;

                default:
                    shopFront.displayIncorrectArguments();
                    break;
            }
        }
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
            Barcode barcode = Barcode.valueOf(productName.toUpperCase());
            farm.addToCart(barcode);
            shop.displayProductAddSuccess();
        } catch (IllegalArgumentException e) {
            shop.displayInvalidProductName();
        } catch (Exception e) {
            shop.displayProductAddFailed(e.getMessage());
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
            Barcode barcode = Barcode.valueOf(productName.toUpperCase());
            farm.addToCart(barcode, quantity);
            shop.displayProductAddSuccess();
        } catch (IllegalArgumentException e) {
            shop.displayInvalidProductName();
        } catch (Exception e) {
            shop.displayProductAddFailed(e.getMessage());
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
        if (farm.getCustomer(name, Integer.parseInt(phoneNumber)) != null) {
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
        farm.saveCustomer(customer);

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
            // Step 1: Prompt the user for the customer's name
            String customerName = shop.promptForCustomerName();

            // Step 2: Prompt the user for the customer's phone number
            int customerPhoneNumber;
            try {
                customerPhoneNumber = shop.promptForCustomerNumber();
            } catch (NumberFormatException e) {
                // If the phone number is invalid, display an error message and return
                shop.displayInvalidPhoneNumber();
                return;
            }

            // Step 3: Look up the customer in the farm's address book
            Customer customer;
            try {
                customer = farm.getCustomer(customerName, customerPhoneNumber);
            } catch (CustomerNotFoundException e) {
                // If the customer is not found, display an error message and return
                shop.displayCustomerNotFound();
                return;
            }

            // Step 4: Create the appropriate transaction based on the transaction type
            Transaction transaction;
            if ("-s".equals(transactionType) || "-specialsale".equals(transactionType)) {
                // SpecialSaleTransaction
                transaction = new SpecialSaleTransaction(customer);
            } else if ("-c".equals(transactionType) || "-categorised".equals(transactionType)) {
                // CategorisedTransaction
                transaction = new CategorisedTransaction(customer);
            } else {
                // Regular Transaction
                transaction = new Transaction(customer);
            }

            // Step 5: Display the transaction start message
            shop.displayTransactionStart();

            // Step 6: Start the transaction
            try {
                farm.getTransactionManager().setOngoingTransaction(transaction);
            } catch (FailedTransactionException e) {
                // If the transaction fails to start, display an error message
                shop.displayFailedToCreateTransaction();
            }

        } catch (Exception e) {
            // Handle any other unexpected exceptions
            shop.displayProductAddFailed("An unexpected error occurred: " + e.getMessage());
        }
    }
}
