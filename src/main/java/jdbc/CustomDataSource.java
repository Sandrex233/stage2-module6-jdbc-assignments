package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
    }

    public static CustomDataSource getInstance() throws IOException {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    // Load properties from app.properties file
                    Properties props = new Properties();
                    try (InputStream input = new FileInputStream("app.properties")) {
                        props.load(input);
                    }

                    // Get properties values
                    String driver = props.getProperty("postgres.driver");
                    String url = props.getProperty("postgres.url");
                    String name = props.getProperty("postgres.name");
                    String password = props.getProperty("postgres.password");

                    instance = new CustomDataSource(driver, url, password, name);
                }
            }
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }
    @Override
    public Connection getConnection(String username, String password)  {
        throw new UnsupportedOperationException("CustomDataSource does not support getConnection(username, password)");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return DataSource.super.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return DataSource.super.createShardingKeyBuilder();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
