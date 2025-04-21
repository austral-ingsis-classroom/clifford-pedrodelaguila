package edu.austral.ingsis.clifford;

import java.util.List;

public class CommandResult {
  private final Element newCurrentDirectory;
  private final String message;
  private final List<Element> elements;

  public CommandResult(Element newCurrentDirectory, String message, List<Element> elements) {
    this.newCurrentDirectory = newCurrentDirectory;
    this.message = message;
    this.elements = elements;
  }

  public CommandResult(Element newCurrentDirectory, String message) {
    this(newCurrentDirectory, message, null);
  }

  public Element getNewCurrentDirectory() {
    return newCurrentDirectory;
  }

  public String getMessage() {
    return message;
  }

  public List<Element> getElements() {
    return elements;
  }
}
