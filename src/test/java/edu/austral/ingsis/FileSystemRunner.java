package edu.austral.ingsis;

import java.util.List;

public interface FileSystemRunner {
  List<String> executeCommands(List<String> commands);
}
