package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Wool product in the inventory.
 */
public class Wool extends Product {

    private static final Barcode barcode = Barcode.WOOL;

    /**
     * Constructs a new Egg product with the specified barcode and quality.
     */
    public Wool(Quality quality) {
        super(barcode, quality != null ? quality : Quality.REGULAR);
    }

    /**
     * Constructs a new Egg product with the specified quality.
     * If no quality is provided, it defaults to regular quality.
     */
    public Wool() {
        super(barcode, Quality.REGULAR);
    }

}