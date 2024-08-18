package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Wool product in the inventory.
 */
public class Wool extends Product {

    private static final Barcode barcode = Barcode.WOOL;
    private static final Quality quality = Quality.REGULAR;

    /**
     * Constructs a new Wool product with the specified barcode and quality.
     */
    public Wool() {
        super(barcode, quality);
    }

    @Override
    public String getDisplayName() {
        return "Wool";
    }

    @Override
    public int getBasePrice() {
        // Define the base price for Wool, could vary by quality
        return 15;
    }
}