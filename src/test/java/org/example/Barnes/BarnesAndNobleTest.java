package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class BarnesAndNobleTest {

    // Fake implementation of the BookDatabase interface
    private static class FakeBookDatabase implements BookDatabase {
        private Book storedBook;

        @Override
        public Book findByISBN(String ISBN) {
            return storedBook;
        }

        public void addBook(Book book) {
            this.storedBook = book;
        }
    }

    // Fake implementation of the BuyBookProcess interface
    private static class FakeBuyBookProcess implements BuyBookProcess {
        private boolean called = false;

        @Override
        public void buyBook(Book book, int quantity) {
            called = true;
        }

        public boolean wasCalled() {
            return called;
        }
    }

    @DisplayName("specification-based")
    @Test
    void testGetPriceForCart_validOrder() {
        FakeBookDatabase db = new FakeBookDatabase();
        db.addBook(new Book("Invincible 01", 25, 5));

        FakeBuyBookProcess process = new FakeBuyBookProcess();
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("Invincible 01", 1);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertNotNull(summary);
        assertEquals(25.0, summary.getTotalPrice(), 0.01);
        assertTrue(process.wasCalled());
    }

    @DisplayName("structural-based")
    @Test
    void testGetPriceForCart_partialAvailability() {
        FakeBookDatabase db = new FakeBookDatabase();
        db.addBook(new Book("Invincible 19", 30, 1));

        FakeBuyBookProcess process = new FakeBuyBookProcess();
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("Invincible 19", 3);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(30.0, summary.getTotalPrice(), 0.01);
        assertTrue(summary.getUnavailable().containsKey(db.findByISBN("Invincible 19")));
    }
}
