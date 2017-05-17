/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import prir.genetic.technical.Compute;
import prir.genetic.technical.Task;

/**
 *
 * @author itoneer
 */
public class GeneticRemote implements Compute {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Compute engine = new GeneticRemote();
            Compute stub =
                (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (RemoteException e) {
            System.err.println("ComputeEngine exception: ");
            e.printStackTrace();
        }
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        return t.execute();
    }
    
}
