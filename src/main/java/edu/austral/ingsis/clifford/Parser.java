package edu.austral.ingsis.clifford;

public class Parser {
    public Parser() {
    }

    public Command parse(String commandInput) {
        String[] parts = commandInput.trim().split(" ");
        String command = parts[0];

        return switch (command) {
            case "ls" -> new Command.Ls("ls");
            case "mkdir" -> new Command.Mkdir("mkdir");
            case "cd" -> new Command.Cd("cd");
            case "pwd" -> new Command.Pwd("pwd");
            case "touch" -> new Command.Touch("touch");
            case "rm" -> new Command.Rm("rm"); // Si también tenés `rm`
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}

