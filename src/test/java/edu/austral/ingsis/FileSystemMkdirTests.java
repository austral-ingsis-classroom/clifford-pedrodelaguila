package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemMkdirTests {
    FileSystem fileSystem;
    UserRoot userRoot;
    Command.Mkdir mkdir;




    @BeforeEach
    void setUp() {
        // Initialize the FileSystem and UserRoot before each test
        userRoot = new UserRoot();
        fileSystem = new FileSystem(userRoot);
        mkdir = new Command.Mkdir("mkdir");

    }

    @Test
    public void testFileSystemInitialization() {
        // Test the initialization of the FileSystem class
        UserRoot userRoot = new UserRoot();
        fileSystem = new FileSystem(userRoot);
        assertNotNull(fileSystem);
        assertTrue(userRoot.isRoot(), "UserRoot should be initialized as root");
    }

    // Test cases for the Command interface

    @Test
    public void testCommandMkdir() throws Exception {
        // Ejecutar el comando mkdir
        fileSystem.execute(mkdir, userRoot, "testDir");
        // Verificar que el userRoot sigue siendo un directorio
        assertTrue(userRoot.isDirectory());
        Assertions.assertEquals(1, userRoot.getChildren().size());
        // Verificar que el hijo se llama "testDir"
        Assertions.assertEquals("testDir", userRoot.getChildren().get(0).getName());
        assertTrue(userRoot.getChildren().get(0).isDirectory());
    }

    @Test
    public void testMkdirMultipleDirectories() throws Exception {
        fileSystem.execute(mkdir, userRoot, "dir1");
        fileSystem.execute(mkdir, userRoot, "dir2");

        Assertions.assertEquals(2, userRoot.getChildren().size());
    }

    @Test
    public void testMkdirDuplicateNameThrows() throws Exception {
        fileSystem.execute(mkdir, userRoot, "dir1");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fileSystem.execute(mkdir, userRoot, "dir1");
        });

        Assertions.assertEquals("Directory already exists.", exception.getMessage());
    }



}
