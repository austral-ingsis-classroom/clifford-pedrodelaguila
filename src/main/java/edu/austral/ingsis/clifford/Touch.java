package edu.austral.ingsis.clifford;

public final class Touch implements Command<Directory> {
    private final String fileName;

    public Touch(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public CommandResult<Directory> execute(Directory currentDirectory) {
        if (fileName.isEmpty()) {
            return CommandResult.noChange(currentDirectory, "No file name provided");
        }

        if (fileName.contains("/") || fileName.contains(" ")) {
            return CommandResult.noChange(currentDirectory, "Invalid file name");
        }

        if (currentDirectory.findNode(fileName) != null) {
            return CommandResult.noChange(currentDirectory, "'" + fileName + "' already exists");
        }

        File newFile = new File(fileName, currentDirectory, "");

        Directory updatedCurrentDir = currentDirectory.insertNode(newFile);

        Directory newRoot = FileSystem.rebuildBranch(currentDirectory, updatedCurrentDir);

        return new CommandResult<>(newRoot, updatedCurrentDir, "'" + fileName + "' file created");
    }
}

