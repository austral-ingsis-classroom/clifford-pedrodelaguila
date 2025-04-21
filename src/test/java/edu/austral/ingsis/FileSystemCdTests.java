package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSystemCdTests {

  FileSystem fileSystem;
  Command.Cd cd;
  UserRoot root;
  Directory subDir;

  @BeforeEach
  void setUp() {
    cd = new Command.Cd("cd");
    root = new UserRoot();
    fileSystem = new FileSystem(root);

    // Crear subdirectorio "boca" en root
    subDir = new Directory("boca", root, new ArrayList<>(), root);
    root.addChild(subDir);
  }

  @Test
  public void testCdPoint_StaysInSameSubDirectory() throws Exception {
    // Arrange: estás parado en subDir ("boca")
    CommandResult result = cd.execute("cd .", subDir);

    // Assert
    assertSame(subDir, result.getNewCurrentDirectory());
    assertEquals("Changing directory to: " + subDir.getName(), result.getMessage());
  }

  @Test
  public void testCdDoublePoint_MovesToParent() throws Exception {
    CommandResult result = cd.execute("cd ..", subDir);

    assertSame(root, result.getNewCurrentDirectory());
    assertEquals("Changing directory to: " + root.getName(), result.getMessage());
  }

  @Test
  public void testCdDoublePoint_WhenAtRoot_StaysAtRoot() throws Exception {
    CommandResult result = cd.execute("cd ..", root);

    assertSame(root, result.getNewCurrentDirectory());
    assertEquals("Already at root directory.", result.getMessage());
  }

  @Test
  public void testCdToExistingSubdirectory() throws Exception {
    CommandResult result = cd.execute("cd boca", root);
    assertEquals("Changed directory to: boca", result.getMessage());
    assertSame(subDir, result.getNewCurrentDirectory());
  }

  @Test
  public void testCdDoublePoint_ThrowsExceptionWhenNullElement() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              cd.execute("cd ..", null);
            });

    assertEquals("Invalid directory.", exception.getMessage());
  }

  @Test
  public void testCdPoint_ThrowsExceptionWhenElementIsFile() {
    // Crear un archivo con root como padre
    File file = new File("documento.txt", root, root);

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              cd.execute("cd .", file);
            });

    assertEquals("Invalid directory.", exception.getMessage());
  }

  @Test
  public void testCdDoublePoint_ThrowsExceptionWhenElementIsFile() {
    File file = new File("boca.txt", root, root);

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              cd.execute("cd ..", file);
            });

    assertEquals("Invalid directory.", exception.getMessage());
  }
}
