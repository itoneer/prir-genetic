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
public class Specimen implements Serializable, Comparable<Specimen> {

    private double x1;
    private double x2;
    private double x3;
    private double fitness = 0.0;

    public Specimen(double x, double y, double z) {
        x1 = x;
        x2 = y;
        x3 = z;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public void mutate() {
        Random r = new Random();
        int i = r.nextInt(3);
        switch (i) {
            case 0:
                x1 = r.nextDouble() * 100 - 50;
                break;
            case 1:
                x2 = r.nextDouble() * 100 - 50;
                break;
            case 2:
                x3 = r.nextDouble() * 100 - 50;
                break;
        }
    }

    public void crossover(Specimen s) {
        Random r = new Random();
        int start = r.nextInt(4);
        boolean dir = r.nextBoolean(); //true - do góry
        double t;
        switch (start) {
            case 0:
                switchGenes(0, s);
                break;
            case 1:
                if (dir) {
                    switchGenes(0, s);
                    switchGenes(1, s);
                } else {
                    switchGenes(1, s);
                    switchGenes(2, s);
                }
                break;
            case 2:
                switchGenes(2, s);
                break;
            case 3: //tylko środek
                switchGenes(1, s);
                break;
        }
    }

    private void switchGenes(int i, Specimen s) {
        double t;
        switch (i) {
            case 0:
                t = x1;
                x1 = s.getX1();
                s.setX1(t);
                break;
            case 1:
                t = x2;
                x2 = s.getX1();
                s.setX1(t);
                break;
            case 2:
                t = x3;
                x3 = s.getX1();
                s.setX1(t);
                break;
            default:
                throw new IllegalArgumentException("Bledny argument: " + i);
        }
    }

    public double computeFitness() {
        double sum = 0;
        sum += 100 * Math.pow((x2 - Math.pow(x1, 2)), 2) + Math.pow(1 - x1, 2);
        sum += 100 * Math.pow((x3 - Math.pow(x2, 2)), 2) + Math.pow(1 - x2, 2);
        fitness = sum;
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Specimen) {
            Specimen s = (Specimen) obj;
            return x1 == s.x1 && x2 == s.x2 && x3 == s.x3;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x1) ^ (Double.doubleToLongBits(this.x1) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x2) ^ (Double.doubleToLongBits(this.x2) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x3) ^ (Double.doubleToLongBits(this.x3) >>> 32));
        return hash;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Specimen s) {
        return (int) (fitness - s.fitness);
    }

}
