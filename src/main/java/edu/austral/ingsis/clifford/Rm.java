package edu.austral.ingsis.clifford;

public final class Rm implements Command<Directory> {
    private final String name;
    private final boolean removeDirectory;

    public Rm(String name) {
        this(name, false);
    }

    public Rm(String name, boolean removeDirectory) {
        this.name = name;
        this.removeDirectory = removeDirectory;
    }

    @Override
    public CommandResult<Directory> execute(Directory currentDirectory) {
        if (name.isEmpty()) {
            return CommandResult.noChange(currentDirectory, "No element name provided");
        }

        FSNode target = currentDirectory.findNode(name);

        if (target == null) {
            return CommandResult.noChange(currentDirectory, "'" + name + "' does not exist");
        }

        if (target.getCategory() == Category.DIRECTORY && !removeDirectory) {
            return CommandResult.noChange(
                    currentDirectory, "cannot remove '" + name + "', is a directory");
        }

        Directory updatedCurrentDir = currentDirectory.deleteNode(name);
        Directory newRoot = FileSystem.rebuildBranch(currentDirectory, updatedCurrentDir);

        return new CommandResult<>(newRoot, updatedCurrentDir, "'" + name + "' removed");
    }
}
