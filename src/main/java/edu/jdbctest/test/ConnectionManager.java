package edu.jdbctest.test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import edu.jdbctest.test.util.PropertiesUtil;


public class ConnectionManager {
    
    private static final String PASSWORD = "db.password";
    private static final String USERNAME = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size*";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnection;


    static {
        loadDriver();
        initConnectionPool();
    }

    private ConnectionManager() {

    }

    private static void initConnectionPool() {
        String poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnection = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Object proxyConneticon = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class}, 
                    (proxy, method, args) -> method.getName().equals("close")
                    ? pool.add((Connection)proxy)
                    : method.invoke(connection, args));
           pool.add((Connection)proxyConneticon);
           sourceConnection.add(connection);
        }
    }

    private static void loadDriver() {

    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } 
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
               PropertiesUtil.get(URL_KEY),
               PropertiesUtil.get(USERNAME),
               PropertiesUtil.get(PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        sourceConnection.forEach(x -> {
            try {
                x.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
