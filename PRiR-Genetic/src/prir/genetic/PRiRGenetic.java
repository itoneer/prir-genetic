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
 * @author itoneer
 */
public class PRiRGenetic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* do zrobienia:
            - parsowanie argumentów
            (wielkość populacji, ilość pokoleń, dokładność/ilość
             pokoleń bez zmian do stwierdzenia stagnacji itp.)
            - ustalenie: domyślnych prawdop. krzyżowania i mutacji
            - podzielenie populacji na fragmenty
            - rozesłanie fragmentów do wszystkich serwerów
            - wyciągnięcie odpowiedzi od serwerów
        */
        
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
