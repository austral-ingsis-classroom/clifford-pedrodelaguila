package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSystemPwdTests {
  FileSystem fileSystem;
  Command.Pwd pwd;
  UserRoot root;

  @BeforeEach
  void setUp() {
    pwd = new Command.Pwd("pwd");
    root = new UserRoot();
    fileSystem = new FileSystem(root);
  }

  @Test
  void testPwdAtRoot() throws Exception {
    Element rootDir = root.getRoot(); // Suponiendo que el nombre es "root"
    CommandResult result = pwd.execute("pwd", rootDir);

    assertEquals(rootDir.getPath(), result.getMessage());
  }

  @Test
  void testPwdInSubdirectory() throws Exception {
    Directory rootDir = root.getRoot();
    Directory subDir = new Directory("subdir", rootDir, new ArrayList<>(), rootDir.getRoot());
    rootDir.addChild(subDir);

    CommandResult result = pwd.execute("pwd", subDir);

    assertEquals(subDir.getPath(), result.getMessage());
  }

  @Test
  void testPwdInFile() throws Exception {
    Directory rootDir = root.getRoot();
    File file = new File("file.txt", rootDir, rootDir.getRoot());
    rootDir.addChild(file);

    CommandResult result = pwd.execute("pwd", file);

    assertEquals(file.getPath(), result.getMessage());
  }
}
