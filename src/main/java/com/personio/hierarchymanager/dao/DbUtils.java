package com.personio.hierarchymanager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DbUtils {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initDb() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS `employee_relationships` (" +
                            " `employee` varchar(200) NOT NULL," +
                            " `supervisor` varchar(200) NOT NULL," +
                            " PRIMARY KEY (`employee`)" +
                            ");"
            );
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
