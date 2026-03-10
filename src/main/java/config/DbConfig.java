package config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.Objects;

public abstract class DbConfig {
    protected static Jdbi jdbi;

    protected static Jdbi get() {
        if (Objects.isNull(jdbi)) {
            connect();
        }
        return jdbi;
    }

    private static void connect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        System.out.println("jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.dbname);
        dataSource.setURL("jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.dbname);
        dataSource.setUser(DBProperties.username);
        dataSource.setPassword(DBProperties.password);
        try {
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(dataSource);
    }

}
