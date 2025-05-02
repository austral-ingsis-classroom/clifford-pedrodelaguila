package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class CommandInterpreter {
  private FileSystem filesystem;
  private final CommandParser commandParser;

  public CommandInterpreter() {
    this.filesystem = new FileSystem();
    this.commandParser = new CommandParser();
  }

  public List<String> executeCommands(List<String> commandLines) {
    List<String> results = new ArrayList<>();

    for (String commandLine : commandLines) {
      String result = executeCommand(commandLine);
      results.add(result);
    }

    return results;
  }

  private String executeCommand(String commandLine) {
    try {
      Command<Directory> command = commandParser.parse(commandLine);
      Directory currentDirectory = filesystem.getCurrentDirectory();

      CommandResult<Directory> commandResult = command.execute(currentDirectory);

      filesystem = filesystem.apply(commandResult);

      return commandResult.output();
    } catch (IllegalArgumentException e) {
      return e.getMessage();
    }
  }
}
