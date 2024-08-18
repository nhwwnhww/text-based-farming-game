package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Milk product in the inventory.
 */
public class Milk extends Product {

    private static final Barcode barcode = Barcode.MILK;
    private static final Quality quality = Quality.REGULAR;

    /**
     * Constructs a new Milk product with the specified barcode and quality
     */
    @SuppressWarnings("checkstyle:JavadocMethod")
    public Milk() {
        super(barcode, quality);
    }

    @Override
    public String getDisplayName() {
        return "Milk";
    }

    @Override
    public int getBasePrice() {
        // Define the base price for Milk, could vary by quality
        return 10;
    }
}