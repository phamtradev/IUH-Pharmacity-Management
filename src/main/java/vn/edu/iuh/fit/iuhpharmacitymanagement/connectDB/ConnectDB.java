/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author PhamTra
 */
public class ConnectDB {

    private static ConnectDB instance = null;
    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    private ConnectDB() {
        try {
            emf = Persistence.createEntityManagerFactory("IUHPharmacityManagement");
            em = emf.createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }

    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            getInstance();
        }
        return em;
    }

    public static void closeConnection() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    // Hàm test kết nối
    public static boolean testConnection() {
        try {
            EntityManager testEm = getInstance().getEntityManager();
            return testEm != null && testEm.isOpen();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
