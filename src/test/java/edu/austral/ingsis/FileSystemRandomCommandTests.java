package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSystemRandomCommandTests {
  FileSystem fileSystem;
  Command.Pwd pwd;
  Command.Mkdir mkdir;
  Command.Ls ls;
  Command.Cd cd;
  Command.Touch touch;
  Command.Rm rm;
  UserRoot root;

  @BeforeEach
  void setUp() {
    pwd = new Command.Pwd("pwd");
    mkdir = new Command.Mkdir("mkdir");
    ls = new Command.Ls("ls");
    cd = new Command.Cd("cd");
    touch = new Command.Touch("touch");
    rm = new Command.Rm("rm");
    root = new UserRoot();
    fileSystem = new FileSystem(root);
  }

  // Add your test methods here
  @Test
  void testRandomCommand() throws Exception {
    CommandResult result = mkdir.execute("testDir", root.getRoot());
    CommandResult result2 = fileSystem.execute(cd, result.getNewCurrentDirectory(), "cd ..");
    Assertions.assertEquals(root.getRoot(), result2.getNewCurrentDirectory());
  }

  @Test
  void testMkdirCommand() throws Exception {
    CommandResult result = mkdir.execute("myFolder", root.getRoot());
    Assertions.assertEquals(root.getChildren().get(0).getName(), "myFolder");
  }

  @Test
  void testLsCommand() throws Exception {
    mkdir.execute("dir1", root.getRoot());
    mkdir.execute("dir2", root.getRoot());
    CommandResult lsResult = fileSystem.execute(ls, root.getRoot(), "ls");
    Assertions.assertEquals(root.getRoot().getChildren(), lsResult.getElements());
  }
}
