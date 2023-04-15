package org.ecsail.fileio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileIOTest {

    @Test
    void testHostFileExists_whenFileExists(@TempDir Path tempDir) throws IOException {
        // Create a temporary file
        File tempFile = tempDir.resolve("tempFile.txt").toFile();
        assertTrue(tempFile.createNewFile(), "Failed to create temporary file");

        // Call the static method and assert that it returns true
        assertTrue(FileIO.hostFileExists(tempFile.getAbsolutePath()), "Expected method to return true for existing file");
    }

    @Test
    void testHostFileExists_whenFileDoesNotExist(@TempDir Path tempDir) {
        // Create a path to a non-existent file
        String nonExistentFilePath = tempDir.resolve("nonExistentFile.txt").toString();

        // Call the static method and assert that it returns false
        assertFalse(FileIO.hostFileExists(nonExistentFilePath), "Expected method to return false for non-existent file");
    }

    @Test
    void testCheckPath_whenDirectoryDoesNotExist(@TempDir Path tempDir) {
        // Generate a path to a non-existent directory
        String nonExistentDirectoryPath = tempDir.resolve("nonExistentDirectory").toString();

        // Call the static method
        FileIO.checkPath(nonExistentDirectoryPath);

        // Assert that the directory was created
        File createdDirectory = new File(nonExistentDirectoryPath);
        assertTrue(createdDirectory.exists(), "Expected the directory to be created");
        assertTrue(createdDirectory.isDirectory(), "Expected the created path to be a directory");
    }

    @Test
    void testCheckPath_whenDirectoryExists(@TempDir Path tempDir) {
        // Generate a path to an existing directory
        String existingDirectoryPath = tempDir.toString();

        // Call the static method
        FileIO.checkPath(existingDirectoryPath);

        // Assert that the directory still exists
        File existingDirectory = new File(existingDirectoryPath);
        assertTrue(existingDirectory.exists(), "Expected the directory to still exist");
        assertTrue(existingDirectory.isDirectory(), "Expected the existing path to be a directory");
    }


}
