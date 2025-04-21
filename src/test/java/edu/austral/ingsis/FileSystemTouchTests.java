package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTouchTests {

    FileSystem fileSystem;
    UserRoot userRoot;
    Command.Touch touch;
    Element currentDirectory;

    @BeforeEach
    void setUp() {
        userRoot = new UserRoot();
        fileSystem = new FileSystem(userRoot);
        touch = new Command.Touch("touch");
        currentDirectory = userRoot.getRoot(); // Obtener el directorio raíz
    }

    @Test
    void testCreateFile() throws Exception {
        CommandResult result = touch.execute("touch newFile.txt", currentDirectory);

        // Verifica que el archivo se haya creado
        assertTrue(currentDirectory.getChildren().stream()
                .anyMatch(child -> child.getName().equals("newFile.txt")));
        assertEquals("File created: newFile.txt", result.getMessage());
    }

    @Test
    void testFileNameInvalid() {

        // Prueba con un nombre de archivo inválido (por ejemplo, con espacios)
        assertThrows(IllegalArgumentException.class, () -> {
            touch.execute("touch invalid file name.txt", currentDirectory);
        });

        // Verifica que no se haya creado ningún archivo con ese nombre
        assertFalse(currentDirectory.getChildren().stream()
                .anyMatch(child -> child.getName().equals("invalid file name.txt")));
    }

    @Test
    void testInvalidDirectory() {
        Element invalidDirectory = null; // Simula un directorio inválido

        assertThrows(IllegalArgumentException.class, () -> {
            touch.execute("touch newFile.txt", invalidDirectory);
        });
    }

    @Test
    void testExistingFile() throws Exception {
        touch.execute("touch existingFile.txt", currentDirectory);

        // Verifica que el archivo se haya creado
        assertTrue(currentDirectory.getChildren().stream()
                .anyMatch(child -> child.getName().equals("existingFile.txt")));

        // Ejecuta de nuevo el comando para verificar si el comportamiento es el esperado
        CommandResult result = touch.execute("touch existingFile.txt", currentDirectory);
        assertEquals("File created: existingFile.txt", result.getMessage());
    }
}

