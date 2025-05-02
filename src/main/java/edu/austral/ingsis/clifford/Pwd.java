package edu.austral.ingsis.clifford;

public final class Pwd implements Command<Directory> {
  public Pwd() {}

  @Override
  public CommandResult<Directory> execute(Directory directory) {
    String location = directory.getLocation();

    return CommandResult.noChange(directory, location);
  }
}
