/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic.technical;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author itoneer
 */
public interface Compute extends Remote {
    <T> T executeTask(Task<T> t) throws RemoteException;
}
