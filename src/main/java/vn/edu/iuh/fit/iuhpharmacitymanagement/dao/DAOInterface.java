/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author PhamTra
 */
public interface DAOInterface<T, ID> {

    boolean insert(T t);

    boolean update(T t);

    Optional<T> findById(ID id);

    List<T> findAll();

}
