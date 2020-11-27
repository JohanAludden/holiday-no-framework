package com.intuit.jaludden;

import java.io.Closeable;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HolidayDatabase {

    private final Function<Path, Database> databaseFunction;
    private Database database;

    public static HolidayDatabase createNull() {
        return new HolidayDatabase((path) -> new NullDatabase());
    }

    public HolidayDatabase() {
        this(RealDatabase::new);
    }

    private HolidayDatabase(Function<Path, Database> databaseFunction) {
        this.databaseFunction = databaseFunction;
    }

    public void start(Path databasePath) {
        if (isStarted()) {
            throw new RuntimeException("can't start database because it's already running");
        }
        database = databaseFunction.apply(databasePath);
        try (DatabaseConnection ignored = database.getConnection()) {
        } catch (RuntimeException e) {
            database = null;
            throw new RuntimeException("Can't start database due to error", e);
        }
    }

    public boolean isStarted() {
        return database != null;
    }

    public void stop() {
        if (!isStarted()) {
            throw new RuntimeException("can't stop database because it's not started");
        }
        database = null;
    }

    public void executeQuery(String selectStatement, Consumer<DatabaseResult> resultHandler) {
        if (!isStarted()) {
            throw new RuntimeException("can't query a stopped database");
        }
        try (DatabaseConnection con = database.getConnection()) {
            var stmt = con.prepareStatement(selectStatement);
            resultHandler.accept(stmt.executeQuery());
        }
    }

    public void executeInsert(String insertStatement, Object... params) {
        if (!isStarted()) {
            throw new RuntimeException("can't insert into a stopped database");
        }
        try (DatabaseConnection con = database.getConnection()) {
            var stmt = con.prepareStatement(insertStatement);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeInsert();
        }
    }

    public void createTable(String statement) {
        try (DatabaseConnection con = database.getConnection()) {
            DatabaseStatement stmt = con.prepareStatement(statement);
            stmt.executeCreateTable();
        }
    }

    public interface Database {
        DatabaseConnection getConnection();
    }

    public interface DatabaseConnection extends Closeable {
        DatabaseStatement prepareStatement(String sql);

        @Override
        void close();
    }

    public interface DatabaseStatement {
        DatabaseResult executeQuery();

        void setObject(int i, Object o);

        void executeInsert();

        void executeCreateTable();
    }

    public interface DatabaseResult {
        String getString(String columnName);

        boolean next();

    }


    private static class RealDatabase implements Database {

        private Path databasePath;

        public RealDatabase(Path databasePath) {
            this.databasePath = databasePath;
        }

        public DatabaseConnection getConnection() {
            try {
                return new RealDatabaseConnection(DriverManager.getConnection("jdbc:sqlite:" + databasePath));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static class RealDatabaseConnection implements DatabaseConnection {
        private final Connection connection;

        public RealDatabaseConnection(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void close() {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public DatabaseStatement prepareStatement(String sql) {
            try {
                return new RealDatabaseStatement(connection.prepareStatement(sql));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class RealDatabaseStatement implements DatabaseStatement {
        private final PreparedStatement statement;

        public RealDatabaseStatement(PreparedStatement statement) {
            this.statement = statement;
        }

        @Override
        public DatabaseResult executeQuery() {
            try {
                return new RealDatabaseResult(statement.executeQuery());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void setObject(int i, Object o) {
            try {
                statement.setObject(i, o);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void executeInsert() {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void executeCreateTable() {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class RealDatabaseResult implements DatabaseResult {

        private final ResultSet rs;

        public RealDatabaseResult(ResultSet rs) {
            this.rs = rs;
        }

        @Override
        public String getString(String columnName) {
            try {
                return rs.getString(columnName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean next() {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class NullDatabase implements Database {

        @Override
        public DatabaseConnection getConnection() {
            return new NullDatabaseConnection();
        }
    }

    private static class NullDatabaseConnection implements DatabaseConnection {
        @Override
        public DatabaseStatement prepareStatement(String sql) {
            return new NullDatabaseStatement();
        }

        @Override
        public void close() {

        }
    }

    private static class NullDatabaseStatement implements DatabaseStatement {
        @Override
        public DatabaseResult executeQuery() {
            return new NullDatabaseResult();
        }

        @Override
        public void setObject(int i, Object o) {
        }

        @Override
        public void executeInsert() {
        }

        @Override
        public void executeCreateTable() {

        }
    }

    private static class NullDatabaseResult implements DatabaseResult {
        private Map<String, String> values = new HashMap<>();

        @Override
        public String getString(String columnName) {
            return values.get(columnName);
        }

        @Override
        public boolean next() {
            return true;
        }
    }
}
