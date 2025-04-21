package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemRmTests {

    FileSystem fileSystem;
    UserRoot userRoot;
    Command.Rm rm;

    @BeforeEach
    void setUp() {
        userRoot = new UserRoot();
        fileSystem = new FileSystem(userRoot);
        rm = new Command.Rm("rm");
    }

    @Test
    void testRemoveFile() throws Exception {
        Directory rootDir = userRoot.getRoot();
        File file = new File("test.txt", rootDir, rootDir.getRoot());
        rootDir.addChild(file);

        CommandResult result = rm.execute("rm test.txt", file);

        assertFalse(rootDir.getChildren().contains(file));
        assertEquals("test.txt removed", result.getMessage());
    }

    @Test
    void testRemoveRootShouldFail() {
        Directory rootDir = userRoot.getRoot();

        assertThrows(IllegalArgumentException.class, () -> {
            rm.execute("rm", rootDir);
        });
    }

    @Test
    void testRemoveEmptyDirectoryRecursive() throws Exception {
        Directory rootDir = userRoot.getRoot();
        Directory dir = new Directory("emptyDir", rootDir, new ArrayList<>(), rootDir.getRoot());
        rootDir.addChild(dir);

        CommandResult result = rm.execute("rm --recursive", dir);

        assertFalse(rootDir.getChildren().contains(dir));
        assertEquals("emptyDir-dir removed", result.getMessage());
    }

    @Test
    void testRemoveDirectoryWithoutRecursiveShouldFail() {
        Directory rootDir = userRoot.getRoot();
        Directory dir = new Directory("nonRecursive", rootDir, new ArrayList<>(), rootDir.getRoot());
        rootDir.addChild(dir);

        assertThrows(IllegalArgumentException.class, () -> {
            rm.execute("rm", dir);
        });
    }

    @Test
    void testRemoveDirectoryWithChildrenRecursive() throws Exception {
        Directory rootDir = userRoot.getRoot();
        Directory parentDir = new Directory("parent", rootDir, new ArrayList<>(), rootDir.getRoot());
        Directory childDir = new Directory("child", parentDir, new ArrayList<>(), rootDir.getRoot());
        File file = new File("file.txt", parentDir, rootDir.getRoot());

        parentDir.addChild(childDir);
        parentDir.addChild(file);
        rootDir.addChild(parentDir);

        CommandResult result = rm.execute("rm --recursive", parentDir);

        assertFalse(rootDir.getChildren().contains(parentDir));
        assertEquals("parent-dir removed", result.getMessage());
    }
}

