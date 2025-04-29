package edu.austral.ingsis.clifford;

import java.util.List;

public class FileSystem {

  Element root;
  CommandResult result;
  Element currentDirectory;

  public FileSystem(Element root, Element currentDirectory, CommandResult result) {
    this.root = root;
    this.result = result;
    this.currentDirectory = currentDirectory;
  }
    public FileSystem() {
        this.root = new Directory("root", null, List.of(), null);
    }


  public FileSystem execute(String commandInput) {
    Parser parser = new Parser();
    Command command = parser.parse(commandInput);
    return command.execute(commandInput, currentDirectory);
  }

  public Element getCurrentDirectory() {
    return currentDirectory;
  }
  public Element getRoot() {
    return root;
  }

    public CommandResult getResult() {
        return result;
    }
}
