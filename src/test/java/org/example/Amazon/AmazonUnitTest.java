package org.example.Amazon;

import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class AmazonUnitTest {

    @DisplayName("specification-based")
    @Test
    void testAmazonCalculateWithRules() {
        // making a fake cart
        ShoppingCart mockCart = mock(ShoppingCart.class);

        // making a fake price rule that adds up prices
        PriceRule mockRule = mock(PriceRule.class);
        when(mockRule.priceToAggregate(mockCart.getItems())).thenReturn(100.0);

        // make a list of one rule
        List<PriceRule> rules = new ArrayList<>();
        rules.add(mockRule);

        // create the Amazon object with fake cart and rule
        Amazon amazon = new Amazon(mockCart, rules);

        // call calculate and make sure total comes out right
        double total = amazon.calculate();
        assertEquals(100.0, total, 0.01);

        // verify that price rule was used
        verify(mockRule).priceToAggregate(mockCart.getItems());
    }

    @DisplayName("structural-based")
    @Test
    void testAddToCart() {
        // make a fake shopping cart
        ShoppingCart mockCart = mock(ShoppingCart.class);
        List<PriceRule> emptyRules = new ArrayList<>();

        // make amazon with empty rules
        Amazon amazon = new Amazon(mockCart, emptyRules);

        // make fake item and try to add it
        Item fakeItem = mock(Item.class);
        amazon.addToCart(fakeItem);

        // check that cart.add() got called
        verify(mockCart).add(fakeItem);
    }

}