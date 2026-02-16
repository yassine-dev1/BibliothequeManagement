package com.UH2.FSTM.ConnexionDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {

    private static 	String jdbcURL 		= "jdbc:mysql://localhost:3308/bibliothequefstm";
    private static  String jdbcUsername = "root";
    private static 	String jdbcPassword = "";

    public ConnectDB() {}

    public static Connection getConnection()
    {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
