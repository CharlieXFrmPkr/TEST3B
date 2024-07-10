package com.example.pc3test3b.controller;

import com.example.pc3test3b.PizzaOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PizzaOrderControllerTest {

    private PizzaOrderController controller;

    @BeforeEach
    public void setUp() {
        controller = new PizzaOrderController();
    }

    @Test
    public void testCalculateTotalBill_XL_OneTopping() {
        double result = controller.calculateTotalBill("XL", 1);
        assertEquals(23.925, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_L_TwoToppings() {
        double result = controller.calculateTotalBill("L", 2);
        assertEquals(17.25, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_XL_MaxToppings() {
        double result = controller.calculateTotalBill("XL", 20);
        assertEquals(51.75, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_L_MaxToppings() {
        double result = controller.calculateTotalBill("L", 20);
        assertEquals(48.3, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_M_MaxToppings() {
        double result = controller.calculateTotalBill("M", 20);
        assertEquals(46.0, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_S_MaxToppings() {
        double result = controller.calculateTotalBill("S", 20);
        assertEquals(43.7, result, 0.001);
    }

    @Test
    public void testCalculateTotalBill_NegativeToppings() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.calculateTotalBill("M", -1);
        });
    }

    @Test
    public void testCalculateTotalBill_NonStandardSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.calculateTotalBill("XXL", 2);
        });
    }

    @Test
    public void testCalculateTotalBill_InvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.calculateTotalBill("Invalid", 3);
        });
    }
}
