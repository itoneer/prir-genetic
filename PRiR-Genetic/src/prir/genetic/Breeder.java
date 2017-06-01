/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import prir.genetic.technical.Task;
import prir.genetic.technical.Tracker;

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
    private final Tracker t;
    
    public Breeder(Population p, int a, int b, double c, double m, Tracker t) {
        this.p = p;
        start = a;
        end = b;
        crossProb = c;
        mutProb = m;
        this.t = t;
    }
    
    @Override
    public Population execute() {
        List<Integer> used = new ArrayList<>();
        Random r = new Random();
        Specimen s1, s2;
        int c;
        for (int i = start; i < end; i++) {
            if (t.isCrossed(i)) continue;
            s1 = p.getSpecimen(i);
            if (r.nextDouble() > crossProb) {
                t.isCrossed(i);
                continue;
            }
            do {
                c = r.nextInt(p.getPopulationSize());
                s2 = p.getSpecimen(c);
            } while (t.isCrossed(c));
            t.isCrossed(i);
            t.isCrossed(c);
            s1.crossover(s2);
        }
        
        for (int i = start; i < end; i++) {
            if (t.isMutated(i) || r.nextDouble() > mutProb) continue;
            s1 = p.getSpecimen(i);
            s1.mutate();
            t.isMutated(i);
        }
        
        return p;
    }
    
}
