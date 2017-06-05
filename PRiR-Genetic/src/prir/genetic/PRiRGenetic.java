/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import prir.genetic.technical.Compute;
import prir.genetic.technical.Task;
import prir.genetic.technical.Tracker;

/**
 * Klasa kontrolera programu.
 * 
 * 
 * @author itoneer, jp
 */
public class PRiRGenetic {

    /**
     * @param args the command line arguments
     */
    private static int population = 100;     //wielkość populacji
    private static int generation = 1000;     //ilość pokoleń
    private static int stagLimit = 250;     //ilość pokoleń bez zmian do stwierdzenia stagnacji
    private static double mutProb = 0.05;
    private static double crossProb = 0.4;
    //private static int numServers = 0;
    private static final StringBuilder HELP = new StringBuilder();
    private static final boolean [] SET = {false, false, false, false, false};
    
    public static void main(String[] args) {
        //StringBuilder do helpa - jeszcze do zrobienia
        HELP.append("Rozproszony Algorytm Genetyczny - Help\n\n");
        HELP.append("Opcje:\n");
        HELP.append("-p, --population - wielkosc populacji <5 - 10000>\n");
        HELP.append("-g, --generation - ilosc pokoleń <20 - 10000>\n");
        HELP.append("-s, --stagnation - ilosc pok. do stwierdzenia stagnacji (0 - brak limitu)\n");
        HELP.append("-c, --crossover - prawdopodobienstwo wystapienia krzyzowania <0.01 - 1>\n");
        HELP.append("-m, --mutation - prawdopodobienstwo wystapienia mutacji <0.01 - 1>\n");
       // help.append("-s, --servers - ilość dostępnych serwerów zewnętrznych (wymagany, co najmniej 1)");
        HELP.append("-h, --help - wyswietlenie tego tekstu\n");
        
        Compute comp1, comp2;
        Specimen best = null;
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry("localhost");
            comp1 = (Compute) registry.lookup(name); //tu też tablica?
            comp2 =(Compute) new GeneticRemote();
        } catch (NotBoundException | RemoteException e) {
            System.err.println("PRiRGenetic exception: Failed to create or bind computing servers");
            e.printStackTrace();
            System.out.print(HELP.toString());
            return;
        }
        
        //Parsowanie argumentów
        for (int i = 0 ; i < args.length ; i += 2)
        {
            switch(args[i]) {
                case "-p":
                case "--population":
                    if (SET[0]){
                        System.out.print(HELP.toString());
                        return;
                    }
                    population = Integer.parseInt(args[i+1]);
                    if (population < 5 || population > 10000) {
                        System.out.println("Blad: niedopuszczalny rozmiar populacji");
                        System.out.print(HELP.toString());
                        return;
                    }
                    SET[0] = true;
                    break;
                case "-g":
                case "--generation":
                    if (SET[1]) {
                        System.out.print(HELP.toString());
                        return;
                    }
                    generation = Integer.parseInt(args[i+1]);
                    if (generation < 20 || generation > 10000) {
                        System.out.println("Blad: niedopuszczalna liczba pokolen");
                        System.out.print(HELP.toString());
                        return;
                    }
                    SET[1] = true;
                    break;
                case "-s":
                case "--stagnation":
                    if (SET[2]) {
                        System.out.print(HELP.toString());
                        return;
                    }
                    stagLimit = Integer.parseInt(args[i+1]);
                    SET[2] = true;
                    break;
                case "-c":
                case "--crossover":
                    if (SET[3]) {
                        System.out.print(HELP.toString());
                        return;
                    }
                    crossProb = Double.parseDouble(args[i+1]);
                    SET[3] = true;
                    break;
                case "-m":
                case "--mutation":
                    if (SET[4]) {
                        System.out.print(HELP.toString());
                        return;
                    }
                    mutProb = Double.parseDouble(args[i+1]);
                    SET[4] = true;
                    break;
                case "-h":
                case "--help":
                    System.out.print(HELP.toString());
                    return;
                default:
                    System.out.println("wtf r u doin/Fakju axaxaxa");
                    System.out.print(HELP.toString());
                    return;
            }
        }
        
        Population p = new Population(population);
        Tracker t = new Tracker((int) (0.35*population));
        int stag = 0;
        for (int i = 0; i < generation; i++){
           // System.out.println("Pokolenie " + (i+1));
            Population p1 = new Population(p, 0, p.getPopulationSize()/2);
            Population p2 = new Population(p, p.getPopulationSize()/2, p.getPopulationSize()-1);
            Task<Population> t1 = new Fitter(p1);
            Task<Population> t2 = new Fitter(p2);
            try {
                p1 = (Population) comp1.executeTask(t1);
                p2 = (Population) comp2.executeTask(t2);
            } catch (RemoteException e) {
                System.err.println("PRiRGenetic exception: Failed to compute fitness in generation " + i);
                e.printStackTrace();
                return;
            }
            Population [] ps = {p1, p2};
            p = new Population(ps);
            p.sort();
            
            p.cull(0.35);
            //System.out.println(p.toString());
            t1 = new Breeder(p, 0, p.getPopulationSize()/2, crossProb, mutProb, t);
            t2 = new Breeder(p, p.getPopulationSize()/2, p.getPopulationSize()-1, crossProb, mutProb, t);
            try {
                comp1.executeTask(t1);
                comp2.executeTask(t2);
            } catch (RemoteException ex ) {
                ex.printStackTrace();
                return;
            }
            t.reset();
            p.fill(population);
            Specimen genBest = p.getBest();
            System.out.println("Najlepszy w pokoleniu " + (i+1) + ": " + genBest.toString() + "\n");
            if (best == null || best.getFitness() > genBest.getFitness()) {
                best = genBest;
                stag = 0;
            }
            else {
                stag++;
                if (stagLimit >0 && stag >= stagLimit) {
                    System.out.println("Osiagnieto dopuszczalny limit pokolen stagnacji.");
                    break;
                }
            }
        }
        
        System.out.println("Najlepiej przystosowany osobnik:");
        System.out.println("x1 = " + best.getX1());
        System.out.println("x2 = " + best.getX2());
        System.out.println("x3 = " + best.getX3());
        System.out.println("Wartosc funkcji przystosowania: " + best.getFitness());
    }    
}