/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.io.Serializable;
import prir.genetic.technical.Task;

/**
 *
 * @author Sławomir
 */
public class Fitter implements Serializable, Task<Population>{ //TODO: serializacja

    private final Population p;
    
    public Fitter (Population p) {
        this.p = p;
    }
    
    @Override
    public Population execute() {
        
        for (int i = 0; i < p.getPopulationSize(); i++) 
            p.getSpecimen(i).computeFitness();
        
        p.sort();
        return p;
    }
    
}
