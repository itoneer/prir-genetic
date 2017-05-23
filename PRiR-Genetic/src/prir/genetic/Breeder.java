/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import prir.genetic.technical.Task;

/**
 *
 * @author Sławomir
 */
public class Breeder implements Serializable, Task<Population> { //TODO: execute, serializacja

    private final Population p;
    private final int start;
    private final int end;
    private final double crossProb;
    private final double mutProb;
    
    public Breeder(Population p, int a, int b, double c, double m) {
        this.p = p;
        start = a;
        end = b;
        crossProb = c;
        mutProb = m;
    }
    
    @Override
    public Population execute() {
        List<Integer> used = new ArrayList<>();
        for (int i = start; i < end; i++) { //TODO: dokończyć
            
        }
        return p;
    }
    
}
