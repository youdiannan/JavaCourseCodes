package com.geekbang;

import java.sql.*;

public class JdbcDemo01 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("start at: " + startTime);

        Class.forName("com.mysql.cj.jdbc.Driver");

        // 局域网内另一台机子
        String url = "jdbc:mysql://192.168.101.95/test?characterEncoding=UTF-8";
        String username = "demo";
        String pwd = "Aa123456";

        Connection connection = DriverManager.getConnection(url, username, pwd);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM STUDENT");
        while (resultSet.next()) {
            printStudent(resultSet);
        }

        System.out.println("======= insert =======");
        int i = statement.executeUpdate("INSERT INTO STUDENT (StudentNo, Name) VALUES (20220103, '老王')");
        if (i == 0) {
            System.out.println("insert failed");
            return;
        }

        resultSet = statement.executeQuery("SELECT * FROM STUDENT");
        while (resultSet.next()) {
            printStudent(resultSet);
        }

        System.out.println("======= delete =======");
        statement.executeUpdate("DELETE FROM STUDENT WHERE Name='老王'");
        resultSet = statement.executeQuery("SELECT * FROM STUDENT");
        while (resultSet.next()) {
            printStudent(resultSet);
        }

        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("end at: " + end);
        System.out.println((end - startTime) + " ms passed");
    }

    public static void printStudent(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(1);
        long studentNo = resultSet.getLong(2);
        String name = resultSet.getString(3);
        System.out.println(String.format("id=%d, studentNo=%d, name=%s", id, studentNo, name));
    }

}
