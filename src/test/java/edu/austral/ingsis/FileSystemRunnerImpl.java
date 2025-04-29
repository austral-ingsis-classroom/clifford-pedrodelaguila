package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.FileSystem;

import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

    private FileSystem fileSystem;
    public FileSystemRunnerImpl() {
        // Inicializa el sistema de archivos vacío, o con la estructura básica que prefieras
        this.fileSystem = new FileSystem();
    }
    @Override
    public List<String> executeCommands(List<String> commands) {
        List<String> results = new ArrayList<>();
        for (String command : commands) {
            // Lógica para ejecutar los comandos y agregar resultados
            results.add(executeCommand(command));
        }
        return results;
    }

    private String executeCommand(String commandInput) {
        // Parsear y ejecutar el comando aquí (ej. mkdir, ls, cd, etc.)
        // El sistema puede usar la clase Command y sus implementaciones (Cd, Ls, Mkdir, etc.)

        String[] parts = commandInput.split(" ");
        String command = parts[0];

        switch (command) {
            case "mkdir":
                return new Command.Mkdir("mkdir").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            case "ls":
                return new Command.Ls("ls").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            case "cd":
                return new Command.Cd("cd").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            case "rm":
                return new Command.Rm("rm").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            case "pwd":
                return new Command.Pwd("pwd").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            case "touch":
                return new Command.Touch("touch").execute(commandInput, fileSystem.getCurrentDirectory()).getResult().getMessage();
            default:
                return "Unknown command";
        }
    }
}
