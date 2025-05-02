package edu.austral.ingsis.clifford;

public final class Mkdir implements Command<Directory> {
  private final String directoryName;

  public Mkdir(String directoryName) {
    this.directoryName = directoryName;
  }

  @Override
  public CommandResult<Directory> execute(Directory currentDirectory) {
    if (directoryName.isEmpty()) {
      return CommandResult.noChange(currentDirectory, "No directory name provided");
    }

    if (directoryName.contains("/") || directoryName.contains(" ")) {
      return CommandResult.noChange(currentDirectory, "Invalid directory name");
    }

    if (currentDirectory.findNode(directoryName) != null) {
      return CommandResult.noChange(currentDirectory, "'" + directoryName + "' already exists");
    }

    Directory newDirectory = new Directory(directoryName, currentDirectory);

    Directory updatedCurrentDir = currentDirectory.insertNode(newDirectory);

    Directory newRoot = FileSystem.rebuildBranch(currentDirectory, updatedCurrentDir);

    return new CommandResult<>(
        newRoot, updatedCurrentDir, "'" + directoryName + "' directory created");
  }
}
