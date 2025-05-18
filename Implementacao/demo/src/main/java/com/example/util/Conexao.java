package demo.src.main.java.com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
   private static final String URL = "jdbc:mysql://localhost:3306/moeda_estudantil";
   private static final String USER = "root";
   private static final String PASSWORD = "SQL.2917";

   public static Connection getConnection() throws Exception {
      return DriverManager.getConnection(URL, USER, PASSWORD);
   }
}
