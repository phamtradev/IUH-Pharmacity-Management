/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.session;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;

/**
 *
 * @author User
 */
public final class SessionManager {

     private static SessionManager instance;
    private NhanVien currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public NhanVien getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(NhanVien user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }
}
