package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public sealed interface Command
        permits Command.Cd, Command.Ls, Command.Mkdir, Command.Touch, Command.Rm, Command.Pwd {

  FileSystem execute(String name, Element element);

  String getName();

  record Cd(String commandName) implements Command {
    @Override
    public FileSystem execute(String commandInput, Element directory){
      switch (commandInput) {
        case "cd .." -> {
          return cdPointPoint(commandInput, directory);
        }
        case "cd ." -> {
          return cdPoint(commandInput, directory);
        }
        default -> {
          String[] parts = commandInput.split(" ");
          if (parts.length == 2 && parts[0].equals("cd")) {
            return cd(parts[1], directory);
          } else {
            return new FileSystem(directory.getRoot(), directory, new CommandResult(directory, "Invalid command."));
          }
        }
      }
    }

    @Override
    public String getName() {
      return commandName;
    }

    private FileSystem cd(String name, Element element) {
      if (element == null || !element.isDirectory()) {
        return new FileSystem(element.getRoot(), element, new CommandResult(element, "Invalid element to make cd."));
      }
      List<Element> children = element.getChildren();
      for (Element child : children) {
        if (child instanceof Directory && child.getName().equals(name)) {
          return new FileSystem(element.getRoot(), child, new CommandResult(child, "Changed directory to: " + child.getName()));
        }
      }
      return new FileSystem(element.getRoot(), element, new CommandResult(element, "No such directory."));
    }

    private FileSystem cdPointPoint(String name, Element element) {
      if (element == null || !element.isDirectory()) {
        return new FileSystem(element.getRoot(), element, new CommandResult(element, "Invalid element to make cd."));
      }
      Element parent = element.getParent();
      if (parent != null) {
        return new FileSystem(element.getRoot(), parent, new CommandResult(parent, "Changing directory to: " + parent.getName()));
      } else {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Already at root."));
      }
    }

    private FileSystem cdPoint(String name, Element element) {
      if (element != null && element.isDirectory()) {
        if (element.getParent() != null) {
          return new FileSystem(element.getRoot(), element , new CommandResult(element, "Staying in the same directory: " + element.getName()));
        } else {
          return new FileSystem(element, element.getRoot(),new CommandResult(element.getRoot(), "Already at root directory."));
        }
      } else {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid element to make cd."));
      }
    }
  }

  record Ls(String commandName) implements Command {
    @Override
    public FileSystem execute(String commandInput, Element element){
      switch (commandInput) {
        case "ls" -> {
          return ls(Order.NONE, element);
        }
        case "ls --ord=asc" -> {
          return ls(Order.ASC, element);
        }
        case "ls --ord=desc" -> {
          return ls(Order.DESC, element);
        }
        default -> {
          return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid command."));
        }
      }
    }

    private FileSystem ls(Order order, Element element) {
      if (element == null || !element.isDirectory()) {
        return new FileSystem(element.getRoot(), element, new CommandResult(element, "Invalid element to make ls."));
      }
      if (element.isLeaf()) {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "This is a leaf element."));
      } else {
        List<Element> children = new ArrayList<>(element.getChildren());
        switch (order) {
          case ASC -> {
            children.sort(Comparator.comparing(Element::getName));
            return new FileSystem(element.getRoot(), element , new CommandResult(element, "Listing contents in ascending order."));
          }
          case DESC -> {
            children.sort(Comparator.comparing(Element::getName).reversed());
            return new FileSystem(element.getRoot(), element , new CommandResult(element, "Listing contents in descending order."));
          }
          case NONE -> {
            return new FileSystem(element.getRoot(), element ,  new CommandResult(element, "Listing contents in default order."));
          }
          default -> {
            return new FileSystem(element.getRoot(),element, new CommandResult(element, "Invalid order."));
          }
        }
      }
    }

    @Override
    public String getName() {
      return commandName;
    }
  }

  public record Mkdir(String commandName) implements Command {

    @Override
    public FileSystem execute(String commandInput, Element element){
      return mkdir(commandInput, element);
    }

    private FileSystem mkdir(String newDirectoryName, Element element){
      if (element == null || !element.isDirectory()) {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid element to make mkdir."));
      }

      if (newDirectoryName == null
              || newDirectoryName.isEmpty()
              || newDirectoryName.contains("/")
              || newDirectoryName.contains(" ")) {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid directory name."));
      }

      Directory currentDir = (Directory) element;

      Directory newDirectory = new Directory(newDirectoryName, currentDir, List.of(), element.getRoot());

      // Create a new directory by replacing the list of children
      List<Element> updatedChildren = new ArrayList<>(currentDir.getChildren());
      updatedChildren.add(newDirectory);
      Directory updatedDir = new Directory(currentDir.getName(), currentDir.getParent(), updatedChildren, currentDir.getRoot());

      String message = "'" + newDirectoryName + "' directory created";
      return new FileSystem(currentDir.getRoot(), updatedDir , new CommandResult(updatedDir, message));
    }

    @Override
    public String getName() {
      return commandName;
    }
  }

  record Touch(String commandName) implements Command {
    @Override
    public FileSystem execute(String commandInput, Element currentDirectory){
      String[] parts = commandInput.trim().split(" ", 2);
      if (parts.length < 2) {
        return new FileSystem(currentDirectory.getRoot(), currentDirectory , new CommandResult(currentDirectory, "Invalid command."));
      }
      String newFileName = parts[1];
      return touch(newFileName, currentDirectory);
    }

    private FileSystem touch(String newFileName, Element currentDirectory) {
      if (currentDirectory == null || !currentDirectory.isDirectory()) {
        return new FileSystem(currentDirectory.getRoot(), currentDirectory , new CommandResult(currentDirectory, "Invalid directory."));
      }
      if (newFileName == null
              || newFileName.isEmpty()
              || newFileName.contains("/")
              || newFileName.contains(" ")) {
        return new FileSystem(currentDirectory.getRoot(), currentDirectory ,  new CommandResult(currentDirectory, "Invalid file name."));
      }
      File newFile = new File(newFileName, currentDirectory, currentDirectory.getRoot());

      List<Element> updatedChildren = new ArrayList<>(currentDirectory.getChildren());
      updatedChildren.add(newFile);
      Directory updatedDir = new Directory(currentDirectory.getName(), currentDirectory.getParent(), updatedChildren, currentDirectory.getRoot());

      return new FileSystem(currentDirectory.getRoot(), updatedDir , new CommandResult(updatedDir, "File created: " + newFileName));
    }

    @Override
    public String getName() {
      return commandName;
    }
  }

  record Rm(String commandName) implements Command {
    @Override
    public FileSystem execute(String commandInput, Element element){
      boolean recursive = commandInput.contains("--recursive");
      return rm(element, recursive);
    }

    private FileSystem rm(Element element, boolean recursive){
      if (element == null || element == element.getRoot()) {
        return new FileSystem(element.getRoot(), element, new CommandResult(element, "Invalid element to make rm."));
      }

      Element parentElement = element.getParent();
      if (!parentElement.isDirectory()) {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid element to make rm."));
      }

      if (element instanceof File file) {
        return removeFile(file);
      }

      if (element instanceof Directory dir) {
        return removeDirectory(dir, recursive);
      }

      throw new IllegalArgumentException("Unknown element type.");
    }

    private FileSystem removeFile(File file) {
      Directory currentDir = (Directory) file.getParent();
      List<Element> updatedChildren = new ArrayList<>(currentDir.getChildren());
      updatedChildren.remove(file);

      Directory updatedDir = new Directory(currentDir.getName(), currentDir.getParent(), updatedChildren, currentDir.getRoot());
      return new FileSystem(updatedDir.getRoot(), updatedDir , new CommandResult(updatedDir, "File removed."));
    }

    private FileSystem removeDirectory(Directory dir, boolean recursive) {
      if (!recursive) {
        return new FileSystem(dir.getRoot(), dir , new CommandResult(dir, "Invalid element to remove."));
      }

      for (Element child : new ArrayList<>(dir.getChildren())) {
        if (child instanceof File file) {
          removeFile(file);
        } else if (child instanceof Directory subDir) {
          removeDirectory(subDir, true);
        }
      }

      Directory parentDir = (Directory) dir.getParent();
      List<Element> updatedChildren = new ArrayList<>(parentDir.getChildren());
      updatedChildren.remove(dir);
      Directory updatedParent = new Directory(parentDir.getName(), parentDir.getParent(), updatedChildren, parentDir.getRoot());

      return new FileSystem(updatedParent.getRoot(), updatedParent , new CommandResult(updatedParent, dir.getName() + " directory removed"));
    }

    @Override
    public String getName() {
      return commandName;
    }
  }

  record Pwd(String commandName) implements Command {
    @Override
    public FileSystem execute(String commandInput, Element element){
      if (commandInput == null || commandInput.isEmpty() || !commandInput.equals("pwd")) {
        return new FileSystem(element.getRoot(), element , new CommandResult(element, "Invalid command."));
      }
      return pwd(element);
    }

    private FileSystem pwd(Element element) {
      return new FileSystem(element.getRoot(), element , new CommandResult(element, "Path of element " + element.getName() + ": " + element.getPath()));
    }

    @Override
    public String getName() {
      return commandName;
    }
  }
}
