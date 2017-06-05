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
        for (int i = 0; i < size; i++) {
            x = r.nextDouble() * 20 - 10;
            y = r.nextDouble() * 20 - 10;
            z = r.nextDouble() * 20 - 10;
            population.add(new Specimen(x, y, z));
        }
    }

    public Population(List<Specimen> s) {
        population = s;
    }

    public Population(Population[] ps) {
        population = new ArrayList<>();
        for (Population p : ps) {
            List<Specimen> ls = p.getPopulation();
            ls.forEach((s) -> {
                population.add(s);
            });
        }
    }

    public Population(Population p, int start, int end) {
        population = new ArrayList<>(p.population.subList(start, end));
    }

    public int getPopulationSize() {
        return population.size();
    }

    /**
     * Znajduje najlepiej przystosowanego osobnika w populacji.
     *
     * @return
     */
    public Specimen getBest() {

        double fit = 0.0;
        double cf;
        int s = 0;

        for (Specimen sp : population) {
            if (sp.getFitness() == 0.0) cf = sp.computeFitness();
            else cf = sp.getFitness();
            if (cf > fit) {
                fit = cf;
                s = population.indexOf(sp);
            }
        }

        return population.get(s);
    }

    public void sort() {
        population.sort((Specimen o1, Specimen o2) -> (int) o1.getFitness() - (int) o2.getFitness());
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

    /**
     * Redukuje posortowaną populację do określonej części najlepszych osobników.
     * 
     * @param d procent populacji, który należy pozostawić
     */
    public void cull(double d) {
        List<Specimen> p = new ArrayList<>();
        for (int i = 0; i < d*population.size(); i++) {
            p.add(population.get(i));
        }
        population = p;
    }

    public void fill(int p) {
        Random r = new Random();
        List<Specimen> p2 = new ArrayList<>(population);
        for (int i = p2.size(); i < p; i++) {
           int j = r.nextInt(population.size());
           p2.add(new Specimen(population.get(j)));
        }
        population = p2;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Populacja o rozmiarze ").append(population.size()).append("\n");
        for (Specimen sp: population) {
            s.append(sp.toString()).append("\n");
        }
        
        return s.toString();
    }

}
