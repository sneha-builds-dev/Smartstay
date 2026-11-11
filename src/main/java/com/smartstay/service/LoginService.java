package com.smartstay.service;

import com.smartstay.dao.UserDAO;

import java.sql.SQLException;

public class LoginService {

    private final UserDAO userDAO;

    public LoginService() {
        userDAO = new UserDAO();
    }

    public boolean login(String username, String password)
            throws SQLException {

        return userDAO.login(username, password);
    }
}