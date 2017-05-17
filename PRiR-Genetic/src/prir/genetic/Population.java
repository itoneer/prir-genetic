/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.io.Serializable;
import java.util.Random;

/**
 * 
 * @author itoneer
 */
public class Population implements Serializable {
    
    private Specimen [] population;
    private Specimen best;
    
    
    public Population(int size) {
        Random r = new Random();
        double x, y, z;
        population = new Specimen[size];
        for (int i = 0; i < size; i++){
            x = r.nextDouble()*100 - 50;
            y = r.nextDouble()*100 - 50;
            z = r.nextDouble()*100 - 50;
            population[i] = new Specimen(x, y, z);
        }
    }
    
    public Population(Specimen [] s) {
        population = s;
    }
    
    public Population (Population [] ps) {
        int l = 0;
        for (Population p: ps) {
            l += p.getPopulationSize();
        }
        population = new Specimen[l];
        l = 0;
        for (Population p: ps) {
            Specimen [] ls = p.getPopulation();
            for (Specimen s : ls) {
                population[l] = s;
                l++;
            }
        }
    }
    
    public int getPopulationSize() {
        return population.length;
    }

    /**
     * Znajduje najlepiej przystosowanego osobnika w populacji.
     * 
     * 
     * @return
     */
    public Specimen getBest() {
        if (best != null) return best;
        
        double fit = 0.0;
        double cf;
        int s = 0;
        
        for(int i = 0; i < population.length; i++) {
            cf = population[i].getFitness();
            if (cf > fit) {
                fit = cf;
                s = i;
            }
        }
        
        return population[s];
    }
    
    public void setBest(Specimen s) {
        best = s;
    }

    public Specimen[] getPopulation() {
        return population;
    }

    public void setPopulation(Specimen[] population) {
        this.population = population;
    }
    
    
}
