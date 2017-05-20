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
public class Fitter implements Serializable, Task{ //TODO: serializacja

    private final Population p;
    
    public Fitter (Population p) {
        this.p = p;
    }
    
    @Override
    public List<Double> execute() {
        List results = new ArrayList<>();
        
        for (int i = 0; i < p.getPopulationSize(); i++) {
            Specimen s = p.getSpecimen(i);
            results.add(s.getFitness());
        }
        return results;
    }
    
}
