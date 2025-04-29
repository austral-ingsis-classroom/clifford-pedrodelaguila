package edu.austral.ingsis.clifford;

import java.util.List;

public class CommandResult {
  private final Element newCurrentDirectory;
  private final String message;

    public CommandResult(Element newCurrentDirectory, String message) {
        this.newCurrentDirectory = newCurrentDirectory;
        this.message = message;
    }

  public Element getNewCurrentDirectory() {
    return newCurrentDirectory;
  }

  public String getMessage() {
    return message;
  }
}
