package utils;

import java.util.Random;

/**
 * DiaMath
 * @author Santiago Barreiro
 */
public class DiaMath {

    // CONSTANTS
    private static final Random random = new Random();
    
    // ATTRIBUTES
    
    
    // CONSTRUCTORS
    
    
    // GETTERS & SETTERS
    
    
    // METHODS
    public static float randomFloat(float min, float max) {
        return (random.nextFloat() * (max - min)) + min;
    }
    
}
