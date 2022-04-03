package com.geekbang;

import java.sql.*;
import java.util.Arrays;

public class JdbcDemo02 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("start at: " + startTime);

        Class.forName("com.mysql.cj.jdbc.Driver");

        // 局域网内另一台机子
        String url = "jdbc:mysql://192.168.101.95/test?characterEncoding=UTF-8";
        String username = "demo";
        String pwd = "Aa123456";


        Connection connection = DriverManager.getConnection(url, username, pwd);

        // 关闭自动提交
        connection.setAutoCommit(false);

        String queryAllStudents = "SELECT * FROM STUDENT";
        String insertStudent = "INSERT INTO STUDENT (StudentNo, Name) VALUES (?, ?)";
        String updateByName = "UPDATE STUDENT SET NAME=? WHERE NAME=?";
        String deleteStudentByName = "DELETE FROM STUDENT WHERE NAME=?";

        PreparedStatement preparedSelect = connection.prepareStatement(queryAllStudents);
        ResultSet resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        System.out.println("===== insert =====");
        PreparedStatement preparedInsert = connection.prepareStatement(insertStudent);
        preparedInsert.setLong(1, 20220103L);
        preparedInsert.setString(2, "老王");
        preparedInsert.executeUpdate();
        resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        System.out.println("===== update =====");
        PreparedStatement preparedUpdate = connection.prepareStatement(updateByName);
        preparedUpdate.setString(1, "老王");
        preparedUpdate.setString(2, "小明");
        preparedUpdate.executeUpdate();
        resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        System.out.println("===== delete =====");
        PreparedStatement preparedDelete = connection.prepareStatement(deleteStudentByName);
        preparedDelete.setString(1, "老王");
        preparedDelete.executeUpdate();
        resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        // 批处理
        batchInsertDemo(preparedInsert);
        resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        // 回滚事务
        System.out.println("===== rollback =====");
        connection.rollback();
        resultSet = preparedSelect.executeQuery();
        while (resultSet.next()) {
            JdbcDemo01.printStudent(resultSet);
        }

        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("end at: " + end);
        System.out.println((end - startTime) + " ms passed");

    }

    public static void batchInsertDemo(PreparedStatement preparedInsert) throws SQLException {
        System.out.println("======== batch insert =======");
        for (int i = 0; i < 10; i++) {
            long studentNo = 20220110 + i;
            String name = "test" + i;
            String insertSql = String.format("INSERT INTO STUDENT (StudentNo, Name) VALUES (%d, '%s')", studentNo, name);
            preparedInsert.addBatch(insertSql);
        }
        int[] ints = preparedInsert.executeBatch();
        System.out.println("insert " + Arrays.stream(ints).sum() + " times");
    }
}
