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
import java.rmi.server.UnicastRemoteObject;
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
        HELP.append("Pierwszym argumentem *zawsze* musi być adres serwera\n");
        HELP.append("-p, --population - wielkość populacji <5 - 10000>\n");
        HELP.append("-g, --generation - ilość pokoleń <20 - 10000>\n");
        HELP.append("-s, --stagnation - ilość pok. do stwierdzenia stagnacji\n");
        HELP.append("-c, --crossover - prawdopodobieństwo wystąpienia krzyżowania <0.01 - 1>\n");
        HELP.append("-m, --mutation - prawdopodobieństwo wystąpienia mutacji <0.01 - 1>\n");
       // help.append("-s, --servers - ilość dostępnych serwerów zewnętrznych (wymagany, co najmniej 1)");
        HELP.append("-h, --help - wyświetlenie tego tekstu\n");
        
        Compute comp1, comp2;
        Specimen best = null;
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            comp1 = (Compute) registry.lookup(name); //tu też tablica?
            comp2 =(Compute) UnicastRemoteObject.exportObject(new GeneticRemote(), 0);
        } catch (NotBoundException | RemoteException e) {
            System.err.println("PRiRGenetic exception: Failed to create or bind computing servers");
            e.printStackTrace();
            System.out.print(HELP.toString());
            return;
        }
        
        //Parsowanie argumentów
        for (int i = 1 ; i < args.length ; i += 2)
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
                        System.out.println("Błąd: niedopuszczalny rozmiar populacji");
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
                        System.out.println("Błąd: niedopuszczalna liczba pokoleń");
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
        Tracker t = new Tracker((int) 0.35*population);
        int stag = 0;
        for (int i = 0; i < generation; i++){
            Population p1 = new Population(p, 0, population/2);
            Population p2 = new Population(p, population/2, population);
            Task<Population> t1 = new Fitter(p1);
            Task<Population> t2 = new Fitter(p2);
            try {
                p1 = (Population) comp1.executeTask(t1);
                p2 = (Population) comp2.executeTask(t2);
            } catch (RemoteException e) {
                System.err.println("PRiRGenetic exception: Failed to compute fitness in generation " + i);
                e.printStackTrace();
            }
            Population [] ps = {p1, p2};
            p = new Population(ps);
            p.sort();
            
            p.cull(0.35);
            t1 = new Breeder(p, 0, p.getPopulationSize()/2, crossProb, mutProb, t);
            t2 = new Breeder(p, p.getPopulationSize()/2, p.getPopulationSize()-1, crossProb, mutProb, t);
            try {
                comp1.executeTask(t1);
                comp2.executeTask(t2);
            } catch (RemoteException ex ) {
                ex.printStackTrace();
            }
            t.reset();
            Specimen genBest = p.getBest();
            if (best == null || best.getFitness() < genBest.getFitness()) {
                best = genBest;
                stag = 0;
            }
            else {
                stag++;
                if (stag >= stagLimit) {
                    System.out.println("Osiągnięto dopuszczalny limit pokoleń stagnacji.");
                    break;
                }
            }
            p.fill(population);
        }
        
        System.out.println("Najlepiej przystosowany osobnik:");
        System.out.println("x1 = " + best.getX1());
        System.out.println("x2 = " + best.getX2());
        System.out.println("x3 = " + best.getX3());
        System.out.println("Wartośćfunkcji przystosowania: " + best.getFitness());
    }    
}