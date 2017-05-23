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
    private int population = 100;     //wielkość populacji
    private int generation = 1000;     //ilość pokoleń
    private int stagLimit = 250;     //ilość pokoleń bez zmian do stwierdzenia stagnacji
    private double mutProb = 0.05;
    private double crossProb = 0.4;
//    private int numServers = 0;
    private final StringBuilder help = new StringBuilder();
    private final boolean [] set = {false, false, false, false, false};
    
    public void main(String[] args) {
        /* do zrobienia:
            - podzielenie populacji na fragmenty
            - rozesłanie fragmentów do wszystkich serwerów
            - wyciągnięcie odpowiedzi od serwerów
        */
        
        
        //StringBuilder do helpa - jeszcze do zrobienia
        help.append("Rozproszony Algorytm Genetyczny - Help\n\n");
        help.append("Opcje:\n");
        help.append("-p, --population - wielkość populacji <5 - 10000>\n");
        help.append("-g, --generation - ilość pokoleń <20 - 10000>\n");
        help.append("-s, --stagnation - ilość pok. do stwierdzenia stagnacji\n");
        help.append("-c, --crossover - prawdopodobieństwo wystąpienia krzyżowania <0.01 - 1>\n");
        help.append("-m, --mutation - prawdopodobieństwo wystąpienia mutacji <0.01 - 1>\n");
       // help.append("-s, --servers - ilość dostępnych serwerów zewnętrznych (wymagany, co najmniej 1)");
        help.append("-h, --help - wyświetlenie tego tekstu\n");
        
        
        //Parsowanie argumentów
        for (int i = 0 ; i < args.length ; i += 2)
        {
            switch(args[i]) {
                case "-p":
                case "--population":
                    if (set[0]){
                        System.out.print(help.toString());
                        return;
                    }
                    population = Integer.parseInt(args[i+1]);
                    set[0] = true;
                    break;
                case "-g":
                case "--generation":
                    if (set[1]) {
                        System.out.print(help.toString());
                        return;
                    }
                    generation = Integer.parseInt(args[i+1]);
                    set[1] = true;
                    break;
                case "-s":
                case "--stagnation":
                    if (set[2]) {
                        System.out.print(help.toString());
                        return;
                    }
                    stagLimit = Integer.parseInt(args[i+1]);
                    set[2] = true;
                    break;
                case "-c":
                case "--crossover":
                    if (set[3]) {
                        System.out.print(help.toString());
                        return;
                    }
                    crossProb = Double.parseDouble(args[i+1]);
                    set[3] = true;
                    break;
                case "-m":
                case "--mutation":
                    if (set[4]) {
                        System.out.print(help.toString());
                        return;
                    }
                    mutProb = Double.parseDouble(args[i+1]);
                    set[4] = true;
                    break;
                case "-h":
                case "--help":
                    System.out.print(help.toString());
                    return;
                default:
                    System.out.println("wtf r u doin/Fakju axaxaxa");
                    System.out.print(help.toString());
                    return;
            }
        }
        
        Compute comp1 = null, comp2 = null;
        Specimen best;
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            comp1 = (Compute) registry.lookup(name); //tu też tablica?
            comp2 =(Compute) UnicastRemoteObject.exportObject(new GeneticRemote(), 0);
        } catch (NotBoundException | RemoteException e) {
            System.err.println("PRiRGenetic exception:");
            e.printStackTrace();
            return;
        }
        
        Population p = new Population(population);
        for (int i = 0; i < generation; i++){
            Population p1 = new Population(p, 0, population/2);
            Population p2 = new Population(p, population/2, population);
            Task t1 = new Fitter(p1);
            Task t2 = new Fitter(p2);
            try {
                p1 = (Population) comp1.executeTask(t1);
                p2 = (Population) comp2.executeTask(t2);
            } catch (RemoteException e) {
                System.err.println("PRiRGenetic exception:");
                e.printStackTrace();
            }
            Population [] ps = {p1, p2};
            p = new Population(ps);
            p.sort();
            
            //do zrobienia: wywalamy najniższe 65% populacji, rozmnażanie
            //i dopełniamy do pełnej populacji
            //z użyciem comp1, comp2 jak wyżej
        }
        
        //do zrobienia: wypisanie najlepszego elementu
    }    
}