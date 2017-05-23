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
    private int population = 0;     //wielkość populacji
    private int generation = 0;     //ilość pokoleń
    private int stag_limit = 0;     //ilość pokoleń bez zmian do stwierdzenia stagnacji
    private double mutProb = 0;
    private double crossProb = 0;
    private StringBuilder help = new StringBuilder();
    
    public void main(String[] args) {
        /* do zrobienia:
            - parsowanie argumentów
            (wielkość populacji, ilość pokoleń, dokładność/ilość
             pokoleń bez zmian do stwierdzenia stagnacji itp.)
            - ustalenie: domyślnych prawdop. krzyżowania i mutacji
            - podzielenie populacji na fragmenty
            - rozesłanie fragmentów do wszystkich serwerów
            - wyciągnięcie odpowiedzi od serwerów
        */
        
        
        //StringBuilder do helpa - jeszcze do zrobienia
        help.append("Algorytm Genetyczny Czegośtam - Help xD\n\n");
        help.append("But nobody came.\n");
        
        
        //Parsowanie argumentów
        for (int i = 0 ; i < args.length ; i += 2)
        {
            switch(args[i]) {
                case "-p":
                    population = Integer.parseInt(args[i+1]);
                    break;
                case "-n":
                    generation = Integer.parseInt(args[i+1]);
                    break;
                case "-s":
                    stag_limit = Integer.parseInt(args[i+1]);
                    break;
                case "-c":
                    crossProb = Double.parseDouble(args[i+1]);
                    break;
                case "-m":
                    mutProb = Double.parseDouble(args[i+1]);
                    break;
                case "-h":
                    System.out.print(help.toString());
                    return;
                default:
                    System.out.println("Fakju axaxaxa");
                    System.out.print(help.toString());
                    return;
            }
        }
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute"; //zastąpimy tablicą?
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name); //tu też tablica?
            //Population p = new Population();
            //Fitter f = new Fitter();
            
        } catch (NotBoundException | RemoteException e) {
            System.err.println("PRiRGenetic exception:");
            e.printStackTrace();
        }
    }    
}