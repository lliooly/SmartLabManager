package com.shishishi3.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 数据库连接工具类
 * 使用 JDBC 提供一个静态方法来获取数据库连接。
 */
public class DbUtil {

    private static final Properties props = new Properties();

    // 使用静态代码块，在类加载时执行一次，读取配置文件
    static {
        try {
            // 从 classpath 的根路径加载 db.properties 文件
            InputStream in = DbUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            // 如果配置文件加载失败，抛出运行时异常，让问题尽早暴露
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 1. 从配置文件中获取驱动名并加载驱动
            Class.forName(props.getProperty("db.driver"));

            // 2. 从配置文件中获取 URL, 用户名和密码来建立连接
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}