package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSystemLsTests {
  FileSystem fileSystem;
  Command.Ls ls;
  Command.Mkdir mkdir;
  UserRoot root;

  @BeforeEach
  void setUp() {
    ls = new Command.Ls("ls");
    mkdir = new Command.Mkdir("mkdir");
    root = new UserRoot();
    fileSystem = new FileSystem(root);
  }

  @Test
  void testLsDefaultOrder() throws Exception {
    mkdir.execute("A", root.getRoot());
    mkdir.execute("B", root.getRoot());
    mkdir.execute("C", root.getRoot());

    CommandResult result = ls.execute("ls", root.getRoot());

    List<Element> listed = result.getNewCurrentDirectory().getChildren();
    assertEquals(3, listed.size());
    assertEquals("A", listed.get(0).getName()); // Orden de inserción
    assertEquals("B", listed.get(1).getName());
    assertEquals("C", listed.get(2).getName());
  }

  @Test
  void testLsAscendingOrder() throws Exception {
    mkdir.execute("Boca", root.getRoot());
    mkdir.execute("River", root.getRoot());
    mkdir.execute("Racing", root.getRoot());

    CommandResult result = ls.execute("ls --ord=asc", root.getRoot());

    List<Element> listed = result.getElements();
    assertEquals(3, listed.size());
    assertEquals("Boca", listed.get(0).getName());
    assertEquals("Racing", listed.get(1).getName());
    assertEquals("River", listed.get(2).getName());
  }

  @Test
  void testLsDescendingOrder() throws Exception {
    mkdir.execute("zeta", root.getRoot());
    mkdir.execute("alpha", root.getRoot());
    mkdir.execute("beta", root.getRoot());

    CommandResult result = ls.execute("ls --ord=desc", root.getRoot());

    List<Element> listed = result.getElements();
    assertEquals(3, listed.size());
    assertEquals("zeta", listed.get(0).getName());
    assertEquals("beta", listed.get(1).getName());
    assertEquals("alpha", listed.get(2).getName());
  }

  @Test
  void testLsOnEmptyDirectory() throws Exception {
    CommandResult result = ls.execute("ls", root.getRoot());

    assertTrue(result.getNewCurrentDirectory().getChildren().isEmpty());
    assertEquals("This is a leaf element.", result.getMessage());
  }

  @Test
  void testLsInvalidDirectory() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          ls.execute("ls", null);
        });
  }

  @Test
  void testLsInvalidCommand() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          ls.execute("ls --wrongflag", root.getRoot());
        });
  }
}
