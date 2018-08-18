package com.minitechnicus.solucian.machine;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpeedCalculatorTest {

    @Test
    public void testSpeed1(){
        SpeedCalculator speedCalculator = new SpeedCalculator();
        int delay = speedCalculator.rotationSpeedToDelay(3.6D, 360);

        assertEquals(10, delay);
    }

    @Test
    public void testSpeed2(){
        SpeedCalculator speedCalculator = new SpeedCalculator();
        int delay = speedCalculator.rotationSpeedToDelay(1.8D, 360);

        assertEquals(5, delay);
    }

    @Test
    public void testSpeed3(){
        SpeedCalculator speedCalculator = new SpeedCalculator();
        int delay = speedCalculator.rotationSpeedToDelay(1.8D, 180);

        assertEquals(10, delay);
    }
}