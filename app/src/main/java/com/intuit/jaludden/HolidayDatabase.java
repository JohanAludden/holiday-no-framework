package com.intuit.jaludden;

import java.io.Closeable;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HolidayDatabase {

    private final Function<Path, Database> databaseFunction;
    private Database database;

    public static HolidayDatabase createNull() {
        return new HolidayDatabase((path) -> new NullDatabase(Collections.emptyIterator()));
    }

    public static HolidayDatabase createNull(Iterator<Map<String, Object>> result) {
        return new HolidayDatabase((path) -> new NullDatabase(result));
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
            var stmt = con.prepareStatement(statement);
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

    public static class NullDatabase implements Database {

        private final Iterator<Map<String, Object>> results;
        public List<NullDatabaseStatement> statements = new ArrayList<>();

        public NullDatabase(Iterator<Map<String, Object>> result) {
            this.results = result;
        }

        @Override
        public DatabaseConnection getConnection() {
            return new NullDatabaseConnection(statements, results);
        }
    }

    private static class NullDatabaseConnection implements DatabaseConnection {
        private final Iterator<Map<String, Object>> results;
        private final List<NullDatabaseStatement> statements;

        public NullDatabaseConnection(List<NullDatabaseStatement> statements, Iterator<Map<String, Object>> results) {
            this.statements = statements;
            this.results = results;
        }

        @Override
        public DatabaseStatement prepareStatement(String sql) {
            NullDatabaseStatement result = new NullDatabaseStatement(sql, results);
            statements.add(result);
            return result;
        }

        @Override
        public void close() {

        }
    }

    public static class NullDatabaseStatement implements DatabaseStatement {
        public Map<Integer, Object> parameters = new HashMap<>();
        Iterator<Map<String, Object>> results;
        public String sql;

        public NullDatabaseStatement(String sql, Iterator<Map<String, Object>> results) {
            this.sql = sql;
            this.results = results;
        }

        @Override
        public DatabaseResult executeQuery() {
            return new NullDatabaseResult(results);
        }

        @Override
        public void setObject(int i, Object o) {
            parameters.put(i, o);
        }

        @Override
        public void executeInsert() {
        }

        @Override
        public void executeCreateTable() {

        }
    }

    static class NullDatabaseResult implements DatabaseResult {
        private final Iterator<Map<String, Object>> results;
        private Map<String, Object> values = new HashMap<>();

        public NullDatabaseResult(Iterator<Map<String, Object>> results) {
            this.results = results;
        }

        public NullDatabaseResult() {
            this.results = Collections.emptyIterator();
        }

        @Override
        public String getString(String columnName) {
            return (String) values.get(columnName);
        }

        @Override
        public boolean next() {
            var result = results.hasNext();
            values = result ? results.next() : null;
            return result;
        }
    }

    public Database getCurrentDatabase() {
        return database;
    }
}
