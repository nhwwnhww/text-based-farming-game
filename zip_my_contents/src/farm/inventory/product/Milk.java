package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Milk product in the inventory.
 */
public class Milk extends Product {

    private static final Barcode barcode = Barcode.MILK;

    /**
     * Constructs a new Egg product with the specified barcode and quality.
     */
    public Milk(Quality quality) {
        super(barcode, quality != null ? quality : Quality.REGULAR);
    }

    /**
     * Constructs a new Egg product with the specified quality.
     * If no quality is provided, it defaults to regular quality.
     */
    public Milk() {
        this(Quality.REGULAR);
    }

}