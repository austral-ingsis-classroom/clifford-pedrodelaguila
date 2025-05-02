package edu.austral.ingsis.clifford;


public class FileSystem {
  private final Directory rootDirectory;
  private final Directory currentDirectory;

  public FileSystem() {
    this.rootDirectory = new Directory();
    this.currentDirectory = this.rootDirectory;
  }

  private FileSystem(Directory rootDirectory, Directory currentDirectory) {
    this.rootDirectory = rootDirectory;
    this.currentDirectory = currentDirectory;
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public FileSystem apply(CommandResult<Directory> result) {
    if (result.newRoot() == null) {
      Directory targetDir = result.currentPosition();

      if (targetDir != rootDirectory && targetDir != currentDirectory) {
        Directory resolvedDir = findDirectoryByPath(rootDirectory, targetDir.getLocation());
        if (resolvedDir != null) {
          return new FileSystem(rootDirectory, resolvedDir);
        }
      }

      return new FileSystem(rootDirectory, targetDir);
    }

    return new FileSystem(result.newRoot(), result.currentPosition());
  }

  private Directory findDirectoryByPath(Directory searchRoot, String path) {
    if (path.equals("/")) {
      return rootDirectory;
    }

    if (!path.startsWith("/")) {
      path = "/" + path;
    }

    String[] pathSegments = path.split("/");
    Directory current = searchRoot;

    for (int i = 1; i < pathSegments.length; i++) {
      String segment = pathSegments[i];
      if (segment.isEmpty()) continue;

      FSNode FSNode = current.findNode(segment);
      if (FSNode == null || FSNode.getCategory() != Category.DIRECTORY) {
        return null;
      }

      current = (Directory) FSNode;
    }

    return current;
  }

  public static Directory rebuildBranch(Directory originalDirectory, Directory modifiedDirectory) {
    if (originalDirectory.parent() == null) {
      return modifiedDirectory;
    }

    Directory newParent = originalDirectory.parent().deleteNode(originalDirectory.getName());
    newParent = newParent.insertNode(modifiedDirectory);

    return rebuildBranch(originalDirectory.parent(), newParent);
  }
}
