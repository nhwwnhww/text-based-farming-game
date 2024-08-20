package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategorisedTransactionTest {

    private Customer customer;
    private CategorisedTransaction transaction;

    @Before
    public void setUp() {
        customer = new Customer("Test Customer", 123456, "123 Test Street");
        customer.getCart().addProduct(new Egg());
        customer.getCart().addProduct(new Milk());
        customer.getCart().addProduct(new Jam());
        customer.getCart().addProduct(new Wool());
        customer.getCart().addProduct(new Milk());
        customer.getCart().addProduct(new Egg());
        customer.getCart().addProduct(new Milk());

        transaction = new CategorisedTransaction(customer);
    }

    @Test
    public void getPurchaseQuantityTest() {
        assertEquals("That is the incorrect number of milk", 3, transaction.getPurchaseQuantity(Barcode.MILK));
        assertEquals("That is the incorrect number of egg", 2, transaction.getPurchaseQuantity(Barcode.EGG));
        assertEquals("That is the incorrect number of jam", 1, transaction.getPurchaseQuantity(Barcode.JAM));
        assertEquals("That is the incorrect number of wool", 1, transaction.getPurchaseQuantity(Barcode.WOOL));
    }

    @Test
    public void getPurchaseSubtotalTest() {
        assertEquals("The subtotal for milk is incorrect!", 1320, transaction.getPurchaseSubtotal(Barcode.MILK));
        assertEquals("The subtotal for egg is incorrect!", 100, transaction.getPurchaseSubtotal(Barcode.EGG));
        assertEquals("The subtotal for jam is incorrect!", 670, transaction.getPurchaseSubtotal(Barcode.JAM));
        assertEquals("The subtotal for wool is incorrect!", 2850, transaction.getPurchaseSubtotal(Barcode.WOOL));
    }

    @Test
    public void getPurchasedTypesTest() {
        Set<Barcode> types = transaction.getPurchasedTypes();
        assertTrue("Expected barcode types not found", types.containsAll(Set.of(Barcode.MILK, Barcode.EGG, Barcode.JAM, Barcode.WOOL)));
    }

    @Test
    public void getPurchasesByTypeTest() {
        Map<Barcode, List<Product>> purchasesByType = transaction.getPurchasesByType();
        assertEquals("You have an error in your map somewhere!",
                Map.of(
                        Barcode.MILK, List.of(new Milk(), new Milk(), new Milk()),
                        Barcode.EGG, List.of(new Egg(), new Egg()),
                        Barcode.JAM, List.of(new Jam()),
                        Barcode.WOOL, List.of(new Wool())
                ),
                purchasesByType);
    }

    @Test
    public void getReceiptJavadocExampleTest() {
        transaction.finalise();
        String expectedReceipt = """
                ================================================
                               The CSSE2002 Farm
                     Building 78, University of Queensland

                                  ╱|、
                                (˚ˎ 。7
                                 |、˜〵
                                 じしˍ,)ノ

                ================================================
                Item       Qty       Price (ea.)      Subtotal
                ------------------------------------------------
                egg        2         $0.50            $1.00
                milk       3         $4.40            $13.20
                jam        1         $6.70            $6.70
                wool       1         $28.50           $28.50
                ------------------------------------------------
                Total:                  $49.40
                ------------------------------------------------
                     Thank you for shopping with us, Test Customer!
                ================================================
                """;
        assertEquals("Receipt does not match example", expectedReceipt.trim(), transaction.getReceipt().trim());
    }

    @Test
    public void getReceiptUnorderedTest() {
        customer.getCart().addProduct(new Jam());
        customer.getCart().addProduct(new Wool());

        transaction = new CategorisedTransaction(customer);
        transaction.finalise();

        String expectedReceipt = """
                ================================================
                               The CSSE2002 Farm
                     Building 78, University of Queensland

                                  ╱|、
                                (˚ˎ 。7
                                 |、˜〵
                                 じしˍ,)ノ

                ================================================
                Item       Qty       Price (ea.)      Subtotal
                ------------------------------------------------
                egg        2         $0.50            $1.00
                milk       3         $4.40            $13.20
                jam        2         $6.70            $13.40
                wool       2         $28.50           $57.00
                ------------------------------------------------
                Total:                  $84.60
                ------------------------------------------------
                     Thank you for shopping with us, Test Customer!
                ================================================
                """;

        assertEquals("Receipt is not ordered correctly or values are wrong", expectedReceipt.trim(), transaction.getReceipt().trim());
    }
}
