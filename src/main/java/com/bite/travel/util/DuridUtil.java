package com.bite.travel.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DuridUtil {
    private static DruidDataSource DRUID_DATA_SOURCE;
    static {
        Properties properties = new Properties();
        InputStream in = DuridUtil.class.getClassLoader().
                getResourceAsStream("durid.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DRUID_DATA_SOURCE= (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        try {
            return DRUID_DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //关闭资源
    public void close(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Connection connection, PreparedStatement preparedStatement){
        close(connection);
        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Connection connection, PreparedStatement preparedStatement,
                      ResultSet resultSet){
        close(connection, preparedStatement);
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
