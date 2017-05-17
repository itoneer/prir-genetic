/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prir.genetic.technical;

/**
 *
 * @author itoneer
 * @param <T> type of the result
 */
public interface Task<T> {
    T execute();
}
