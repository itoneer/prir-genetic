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

/**
 * 
 * @author itoneer
 */
public class Population implements Serializable { //TODO: serializacja
    
    private List<Specimen> population;
    
    public Population(int size) {
        Random r = new Random();
        double x, y, z;
        population = new ArrayList<>();
        for (int i = 0; i < size; i++){
            x = r.nextDouble()*100 - 50;
            y = r.nextDouble()*100 - 50;
            z = r.nextDouble()*100 - 50;
            population.add(new Specimen(x, y, z));
        }
    }
    
    public Population(List<Specimen> s) {
        population = s;
    }
    
    public Population (Population [] ps) {
        population = new ArrayList<>();
        for (Population p: ps) {
            List<Specimen> ls = p.getPopulation();
            ls.forEach((s) -> { 
                population.add(s);
            });   
        }
    }
    
    public int getPopulationSize() {
        return population.size();
    }

    /**
     * Znajduje najlepiej przystosowanego osobnika w populacji.
     * 
     * 
     * @return
     */
    public Specimen getBest() {
        
        double fit = 0.0;
        double cf;
        int s = 0;
        
        for(Specimen sp: population) {
            cf = sp.getFitness();
            if (cf > fit) {
                fit = cf;
                s = population.indexOf(sp);
            }
        }
        
        return population.get(s);
    }
    
    public List<Specimen> getPopulation() {
        return population;
    }

    public void setPopulation(List<Specimen> population) {
        this.population = population;
    }

    public Specimen getSpecimen(int i) {
        return population.get(i);
    }
    
    
}
