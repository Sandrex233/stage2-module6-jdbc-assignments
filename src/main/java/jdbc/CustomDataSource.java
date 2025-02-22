package jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
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

    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    // Load properties from app.properties file
                    Properties props = new Properties();
                    try {
                        props.load(CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties"));

                        // Get properties values
                        String driver = props.getProperty("postgres.driver");
                        String url = props.getProperty("postgres.url");
                        String name = props.getProperty("postgres.name");
                        String password = props.getProperty("postgres.password");

                        instance = new CustomDataSource(driver, url, password, name);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to load properties from app.properties file", e);
                    }
                }
            }
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(instance.getUrl(), instance.getName(), instance.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new CustomConnector().getConnection(instance.getUrl(), instance.getName(), instance.getPassword());
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

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
