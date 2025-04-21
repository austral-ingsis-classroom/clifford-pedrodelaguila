package edu.austral.ingsis.clifford;

public class FileSystem {

  UserRoot userRoot;

  public FileSystem(UserRoot root) {
    this.userRoot = root;
  }

  public CommandResult execute(Command command, Element element, String commandInput)
      throws Exception {
    return command.execute(commandInput, element);
  }
}
