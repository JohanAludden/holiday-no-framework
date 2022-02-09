package com.aldercape.holiday;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SQLiteTest {

    @TempDir
    public Path tempDir;

    @Test
    public void createNewDatabase() throws Exception {


        assertTrue(Files.exists(tempDir));
        assertTrue(Files.isDirectory(tempDir));
        assertFalse(Files.exists(tempDir.resolve("asas")));
        assertFalse(Files.isDirectory(tempDir.resolve("asas")));

        assertThrows(SQLException.class, () -> DriverManager.getConnection("jdbc:sqlite:" + tempDir.resolve("asas").resolve("sqlLite.db")));
    }

}
