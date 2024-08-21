package farm;

//Stage 0
import farm.core.DuplicateCustomerException;
import farm.customer.*;

import java.util.List;

// Stage 1
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.sales.transaction.CategorisedTransaction;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

// Stage 2 + Stage 3
//import farm.inventory.*;

/**
 * Execute the Farm MVP program.
 * This file is for you to execute your program,
 * it will not be marked.
 */
public class Main {

    /**
     * Start the farm program.
     * @param args Parameters to the program, currently not supported.
     */
    @SuppressWarnings("checkstyle:LeftCurly")
    public static void main(String[] args)
         throws DuplicateCustomerException {
        // Stage 1
        // Note as you complete stages, you will need to
        // import their packages or uncomment them above.

        // -- Stage 0: Completion of AddressBook and Customer at stage
        AddressBook addressBook = new AddressBook();
        Customer customer = new Customer("Ali", 33651111, "UQ");
        addressBook.addCustomer(customer);
        for (String name : List.of("James", "Alex", "Lauren")) {
            addressBook.addCustomer(new Customer(name, 1234, "1st Street"));
        }
        System.out.println(addressBook.getAllRecords());

        //// -- Stage 1: Products + Transactions
        System.out.println("\n");
        System.out.println(new Milk());

        Transaction transaction = new Transaction(customer);
        for (int i = 0; i < 3; i++) {
            transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        }
        transaction.getAssociatedCustomer().getCart().addProduct(new Egg());
        transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        transaction.finalise();
        System.out.println("\n");
        System.out.println(transaction.getReceipt());

        transaction = new SpecialSaleTransaction(customer);
        for (int i = 0; i < 3; i++) {
            transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        }
        transaction.getAssociatedCustomer().getCart().addProduct(new Egg());
        transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        transaction.finalise();
        System.out.println("\n".repeat(3));
        System.out.println(transaction.getReceipt());

        // -- Stage 2 + 3: Combining them together

        //Inventory inventory = new BasicInventory();
        //boolean fancy = false;

        // Keep removed for Stage 2 but add when Stage 3 is done
        ////inventory = new FancyInventory();
        ////fancy = true;

        //for (Barcode barcode : List.of(Barcode.MILK, Barcode.EGG, Barcode.WOOL, Barcode.EGG)) {
        //    for (Quality quality : List.of(Quality.REGULAR, Quality.SILVER, Quality.REGULAR,
        //            Quality.GOLD, Quality.REGULAR, Quality.REGULAR, Quality.IRIDIUM)) {
        //        inventory.addProduct(barcode, quality);
        //    }
        //}

        //FarmManager manager = new FarmManager(new Farm(inventory, addressBook),
        //        new ShopFront(), fancy);
        //manager.run();


    }
}