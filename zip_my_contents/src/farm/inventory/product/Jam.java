package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Jam product in the inventory.
 */
public class Jam extends Product {

    private static final Barcode barcode = Barcode.JAM;
    private static final Quality quality = Quality.REGULAR;

    /**
     * Constructs a new Jam product with the specified barcode and quality
     */
    public Jam() {
        super(barcode, quality);
    }

    @Override
    public String getDisplayName() {
        return "Jam";
    }

    @Override
    public int getBasePrice() {
        // Define the base price for Jam, could vary by quality
        return 20;
    }
}