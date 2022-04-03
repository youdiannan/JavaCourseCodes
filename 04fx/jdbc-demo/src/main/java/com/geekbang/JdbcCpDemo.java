package com.geekbang;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Hikari连接池demo
public class JdbcCpDemo {

    public static void main(String[] args) throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("start at: " + startTime);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://192.168.101.95/test?characterEncoding=UTF-8");
        hikariConfig.setUsername("demo");
        hikariConfig.setPassword("Aa123456");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setAutoCommit(false);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        Connection connection = hikariDataSource.getConnection();

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
        JdbcDemo02.batchInsertDemo(preparedInsert);
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
}
