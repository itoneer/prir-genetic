/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic.technical;

import java.util.Arrays;

/**
 *
 * @author itoneer
 */
public class Tracker {
    private final boolean[] mutated;
    private final boolean[] crossed;
    private final int size;
    
    public Tracker(int size) {
        mutated = new boolean[size];
        Arrays.fill(mutated, false);
        
        crossed = new boolean[size];
        Arrays.fill(crossed, false);
        this.size = size;
    }
    
    public boolean isMutated(int i) {
        return mutated[i];
    }
    
    public boolean isCrossed(int i) {
        return crossed[i];
    }
    
    public boolean markAsMutated(int i) {
        synchronized(mutated) {
            try {
                mutated[i] = true;
            } catch(ArrayIndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }
    }
    
    public boolean markAsCrossed(int i) {
        synchronized(crossed) {
            try {
                crossed[i] = true;
            } catch(ArrayIndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }
    }
    
    public void reset() {
        Arrays.fill(mutated, false);
        Arrays.fill(crossed, false);
    }
}
