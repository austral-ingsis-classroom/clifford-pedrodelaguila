package edu.austral.ingsis.clifford;

public final class Cd implements Command<Directory> {
    private final String extra;

    public Cd(String extra) {
        this.extra = extra;
    }

    @Override
    public CommandResult<Directory> execute(Directory currentDirectory) {
        if (extra.isEmpty() || extra.equals(".")) {
            return CommandResult.noChange(currentDirectory, "moved to directory '.'");
        }

        if (extra.equals("..")) {
            Directory parent = currentDirectory.parent();
            if (parent == null) {
                return CommandResult.noChange(currentDirectory, "moved to directory '/'");
            }
            return CommandResult.noChange(parent, "moved to directory '" + parent.getName() + "'");
        }

        if (extra.startsWith("/")) {
            Directory root = getRoot(currentDirectory);

            if (extra.equals("/")) {
                return CommandResult.noChange(root, "moved to directory '/'");
            }

            String relativePath = extra.substring(1);
            return navigateToPath(root, relativePath);
        }

        return navigateToPath(currentDirectory, extra);
    }

    private Directory getRoot(Directory directory) {
        Directory currentDirectory = directory;
        while (currentDirectory.parent() != null) {
            currentDirectory = currentDirectory.parent();
        }
        return currentDirectory;
    }

    private CommandResult<Directory> navigateToPath(Directory startDir, String completePath) {
        String[] pathSegments = completePath.split("/");
        Directory currentDir = startDir;

        for (String segment : pathSegments) {
            if (segment.isEmpty() || segment.equals(".")) {
                continue;
            }

            if (segment.equals("..")) {
                Directory parent = currentDir.parent();
                if (parent == null) {
                    continue;
                }
                currentDir = parent;
                continue;
            }

            FSNode nextFSNode = currentDir.findNode(segment);

            if (nextFSNode == null) {
                return CommandResult.noChange(
                        startDir, "'" + segment + "' directory does not exist");
            }

            if (nextFSNode.getCategory() != Category.DIRECTORY) {
                return CommandResult.noChange(startDir, "'" + segment + "' is not a directory");
            }

            currentDir = (Directory) nextFSNode;
        }

        return CommandResult.noChange(
                currentDir,
                "moved to directory '"
                        + (currentDir.getName().equals("/") ? "/" : currentDir.getName())
                        + "'");
    }
}
