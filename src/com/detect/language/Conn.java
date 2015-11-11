/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detect.language;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Telosma
 */
public class Conn {

    String usn = "root";
    String pwd = "";

    //1. Creat a connection to database

    public Conn() {
    }

    public Statement getStatement(String dbname, String tbname) {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            url += dbname;
            Connection myConn = DriverManager.getConnection(url, usn, pwd);

            //2. Creat a statement
            Statement stm = myConn.createStatement();

            return stm;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;

    }

    public ResultSet getResultSet(String dbname, String tbname, String colname) {
        Statement stm = this.getStatement(dbname, tbname);
        String query = "select distinct ";
        query += colname;
        query += " from ";
        query += tbname;
        try {
            ResultSet rs = stm.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultSet getWord(String word, String dbname, String tbname) {
        Statement stm = this.getStatement(dbname, tbname);
        String query = "select distinct * from ";
        query += tbname;
        query += " where word=\"";
        query += word;
        query += "\"";
        try {
            ResultSet rs = stm.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
