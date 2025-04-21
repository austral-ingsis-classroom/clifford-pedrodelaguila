package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public sealed interface Command permits Command.Cd,
        Command.Ls, Command.Mkdir, Command.Touch, Command.Rm, Command.Pwd{
    CommandResult execute(String name, Element element) throws Exception;
    String getName();

    record Cd(String commandName) implements Command {
        @Override
        public CommandResult execute(String commandInput, Element directory) throws Exception {
            switch (commandInput) {
                case "cd .." -> {
                    return cdPointPoint(commandInput, directory);
                }
                case "cd ." -> {
                    return cdPoint(commandInput, directory);
                }
                default -> {
                    System.out.println("Changing directory to: " + commandInput);
                    return new CommandResult(directory, "Changed directory to: " + commandInput);
                }
            }
        }

        @Override
        public String getName() {
            return commandName;
        }

        private CommandResult cdPointPoint(String name, Element element) {
            // Implementation for changing to parent directory
            if (element == null || !element.isDirectory()) {
                System.out.println("Invalid directory.");
                throw new IllegalArgumentException("Invalid directory.");
            }
            Element parent = element.getParent();
            if (parent != null) {
                System.out.println("Changing directory to: " + parent.getName());
                return new CommandResult( parent,"Changing directory to: " + parent.getName());
            }
            else {
                System.out.println("Already at root directory." + element.getRoot().getName());
                new CommandResult(element.getRoot(), "Already at root directory.");
            }
            return new CommandResult(element, "Already at root directory.");
        }
        private CommandResult cdPoint(String name, Element element) {
            // Implementation stay in same diectory
            if (element != null && element.isDirectory()) {
                if (element.getParent() != null) {
                    System.out.println("Changing directory to: " + element.getParent().getName());
                    return new CommandResult( element, "Changing directory to: " + element.getName());
                } else {
                    System.out.println("Already at root directory." + element.getRoot().getName());
                    return new CommandResult(element.getRoot(), "Already at root directory.");
                }
            }
            throw new IllegalArgumentException("Invalid directory.");
        }
    }

    record Ls(String commandName) implements Command {
        @Override
        public CommandResult execute(String commandInput, Element element) throws Exception {
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
                    System.out.println("Invalid command.");
                    throw new IllegalArgumentException("Invalid command: " + commandInput);
                }
            }
        }

        private CommandResult ls(Order order, Element element) throws Exception {
            if (element == null || !element.isDirectory()) {
                System.out.println("Invalid directory.");
                throw new IllegalArgumentException("Invalid directory.");
            }

            if (element.isLeaf()) {
                System.out.println("This is a leaf element.");
                return new CommandResult(element, "This is a leaf element.", new ArrayList<>());
            }
            else {
                List<Element> children = element.getChildren();
                List<Element> result = new ArrayList<>(children);

                switch (order) {
                    case ASC -> {
                        result.sort(Comparator.comparing(Element::getName));
                        System.out.println("Listing contents in ascending order.");
                        return new CommandResult(element, "Listing contents in ascending order.", result);
                    }
                    case DESC -> {
                        result.sort(Comparator.comparing(Element::getName).reversed());
                        System.out.println("Listing contents in descending order.");
                        return new CommandResult(element, "Listing contents in descending order.", result );
                    }
                    case NONE -> {
                        System.out.println("Listing contents in default order.");
                        return new CommandResult(element , "Listing contents in default order." , result);
                    }
                    default -> {
                        // Este caso no debería ocurrir, pero se incluye por completitud
                        System.out.println("Invalid order.");
                        throw new IllegalArgumentException("Invalid order: " + order);
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
        public CommandResult execute(String commandInput, Element element) throws Exception {
            return mkdir(commandInput, element);
        }

        private CommandResult mkdir(String newDirectoryName, Element element) throws Exception {
            if (element == null || !element.isDirectory()) {
                throw new IllegalArgumentException("Invalid directory.");
            }

            if (newDirectoryName == null || newDirectoryName.isEmpty() ||
                    newDirectoryName.contains("/") || newDirectoryName.contains(" ")) {
                throw new IllegalArgumentException("Invalid directory name.");
            }

            Directory currentDir = (Directory) element;

            // Verificar que no exista ya
            for (Element child : currentDir.getChildren()) {
                if (child.getName().equals(newDirectoryName)) {
                    throw new IllegalArgumentException("Directory already exists.");
                }
            }

            Directory newDirectory = new Directory(newDirectoryName, currentDir, new ArrayList<>(), element.getRoot());
            currentDir.addChild(newDirectory);

            String message = "'" + newDirectoryName + "' directory created";
            System.out.println(message);
            return new CommandResult(currentDir, message);
        }

        @Override
        public String getName() {
            return commandName;
        }
    }


    record Touch(String commandName) implements Command {
        @Override
        public CommandResult execute(String commandInput, Element currentDirectory) throws Exception {
            String[] parts = commandInput.trim().split(" ", 2); // Usamos 2 para dividir en dos partes
            if (parts.length < 2) {
                throw new IllegalArgumentException("Missing file name");
            }

            String newFileName = parts[1];
            return touch(newFileName, currentDirectory);
        }

        private CommandResult touch(String newFileName, Element currentDirectory) {
            // Implementation for creating a file
            if (currentDirectory == null || !currentDirectory.isDirectory()) {
                System.out.println("Invalid directory.");
                throw new IllegalArgumentException("Invalid directory.");
            }
            if (newFileName == null || newFileName.isEmpty() || newFileName.contains("/") || newFileName.contains(" ")) {
                System.out.println("Invalid file name.");
                throw new IllegalArgumentException("Invalid file name.");
            }
            File newFile = new File(newFileName, currentDirectory, currentDirectory.getRoot());
            currentDirectory.getChildren().add(newFile);
            System.out.println(newFileName + "file created");
            return new CommandResult(currentDirectory, "File created: " + newFileName);

        }

        @Override
        public String getName() {
            return commandName;
        }
    }

    record Rm(String commandName) implements Command {
        @Override
        public CommandResult execute(String commandInput, Element element) throws Exception {
            boolean recursive = commandInput.contains("--recursive");
            return rm(element, recursive);
        }

        private CommandResult rm(Element element, boolean recursive) throws Exception {
            if (element == null || element == element.getRoot()) {
                throw new IllegalArgumentException("Cannot remove root directory.");
            }

            Element parentElement = element.getParent();
            if (!parentElement.isDirectory()) {
                throw new IllegalArgumentException("Parent is not a directory.");
            }

            if (element instanceof File file) {
                return removeFile(file);
            }

            if (element instanceof Directory dir) {
                return removeDirectory(dir,recursive );
            }

            throw new IllegalArgumentException("Unknown element type.");
        }

        private CommandResult removeFile(File file) {
            System.out.println("Removing file: " + file.getName());
            Directory currentDir = (Directory) file.getParent();
            currentDir.removeChild(file);
            return new CommandResult(currentDir, file.getName() + " removed");
        }

        private CommandResult removeDirectory(Directory dir, boolean recursive) {
            if (!recursive) {
            throw new IllegalArgumentException("Cannot remove a directory without --recursive.");
        }
            for (Element child : new ArrayList<>(dir.getChildren())) {
                if (child instanceof File file) {
                    dir.removeChild(file);
                } else if (child instanceof Directory subDir) {
                    removeDirectory(subDir, true);
                }
            }
            System.out.println("Removing directory: " + dir.getName());
            Directory currentDir = (Directory) dir.getParent();
            currentDir.removeChild(dir);
            return new CommandResult(currentDir, dir.getName() + "-dir removed");
        }



        @Override
        public String getName() {
            return commandName;
        }
    }

    record Pwd(String commandName) implements Command {
        @Override
        public CommandResult execute(String commandInput, Element element) throws Exception {
            if (commandInput == null || commandInput.isEmpty() || !commandInput.equals("pwd")) {
                throw new IllegalArgumentException("Invalid command.");
            }
            if (element == null || element.getRoot() == null) {
                throw new IllegalArgumentException("Invalid directory.");
            }
            return pwd(element);
        }

        private CommandResult pwd(Element element) {
            return new CommandResult(element, element.getPath());
        }

        @Override
        public String getName() {
            return commandName;
        }
    }



}


