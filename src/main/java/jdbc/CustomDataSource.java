package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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
                    try (InputStream input = new FileInputStream("/home/user/Desktop/Java/MJC School/stage2-module6-jdbc-assignments/src/main/resources/app.properties")) {
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
    public PrintWriter getLogWriter()  {
        throw new UnsupportedOperationException("CustomDataSource does not support getLogWriter()");
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        throw new UnsupportedOperationException("CustomDataSource does not support setLogWriter(PrintWriter)");
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException("CustomDataSource does not support setLoginTimeout(int)");
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("CustomDataSource does not support getLoginTimeout()");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("CustomDataSource does not support getParentLogger()");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("CustomDataSource cannot be unwrapped to " + iface.getName());
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)  {
        return false;
    }
}
