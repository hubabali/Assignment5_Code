package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Cost.RegularCost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;


public class AmazonIntegrationTest {

    @DisplayName("specification-based")
    @Test
    void testAmazonFullFlow() {
        // make a fake database
        Database fakeDB = mock(Database.class);

        // use the real adaptor with fake database
        ShoppingCart cart = new ShoppingCartAdaptor(fakeDB);

        // the items im ordering
        Item thor = new Item(ItemType.ELECTRONIC, "Thor_Figure", 1, 25.0);
        Item cap = new Item(ItemType.ELECTRONIC, "Captain_America", 1, 75.0);


        List<Item> fakeItems = new ArrayList<>();
        fakeItems.add(thor);
        fakeItems.add(cap);
        when(fakeDB.withSql(any())).thenReturn(fakeItems);

        // create price rule and amazon instance
        List<PriceRule> rules = new ArrayList<>();
        rules.add(new RegularCost());
        Amazon amazon = new Amazon(cart, rules);

        // run calculation
        double total = amazon.calculate();
        assertEquals(100.0, total, 0.01);
    }

    @DisplayName("structural-based")
    @Test
    void testEmptyCartGivesZero() {
        Database fakeDB = mock(Database.class);
        ShoppingCart cart = new ShoppingCartAdaptor(fakeDB);

        // no items dis time
        List<Item> empty = new ArrayList<>();
        when(fakeDB.withSql(any())).thenReturn(empty);

        List<PriceRule> rules = new ArrayList<>();
        rules.add(new RegularCost());
        Amazon amazon = new Amazon(cart, rules);

        double total = amazon.calculate();
        assertEquals(0.0, total, 0.01);
    }
}