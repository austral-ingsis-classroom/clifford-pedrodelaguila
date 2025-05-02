package edu.austral.ingsis;

import edu.austral.ingsis.clifford.CommandInterpreter;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {
  private final CommandInterpreter cli = new CommandInterpreter();

  @Override
  public List<String> executeCommands(List<String> commands) {
    return cli.executeCommands(commands);
  }
}
