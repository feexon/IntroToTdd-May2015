package ca.jbrains.pos.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SellOneItemTest {
    @Test
    public void productFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<String, String>() {{
            put("12345", "7,95 EUR");
            put("23456", "12,50 EUR");
        }});

        sale.onBarcode("12345");

        Assert.assertEquals("7,95 EUR", display.getText());
    }

    @Test
    public void anotherProductFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<String, String>() {{
            put("12345", "7,95 EUR");
            put("23456", "12,50 EUR");
        }});

        sale.onBarcode("23456");

        Assert.assertEquals("12,50 EUR", display.getText());
    }

    @Test
    public void productNotFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<String, String>() {{
            put("12345", "7,95 EUR");
            put("23456", "12,50 EUR");
        }});

        sale.onBarcode("99999");

        Assert.assertEquals("Product not found for 99999", display.getText());
    }

    @Test
    public void emptyBarcode() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(display, null);

        sale.onBarcode("");

        Assert.assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Sale {
        private final Map<String, String> pricesByBarcode;
        private final Display display;

        public Sale(Display display, Map<String, String> pricesByBarcode) {
            this.display = display;
            this.pricesByBarcode = pricesByBarcode;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.displayScannedEmptyBarcodeMessage();
                return;
            }

            final String price = findPrice(barcode);
            if (price == null)
                display.displayProductNotFoundMessage(barcode);
            else
                display.displayPrice(price);
        }

        private String findPrice(String barcode) {
            return pricesByBarcode.get(barcode);
        }

    }

    public static class Display {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void displayProductNotFoundMessage(String barcode) {
            setText("Product not found for " + barcode);
        }

        public void displayScannedEmptyBarcodeMessage() {
            setText("Scanning error: empty barcode");
        }

        public void displayPrice(String price) {
            setText(price);
        }
    }
}
