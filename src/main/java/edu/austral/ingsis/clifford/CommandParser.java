package edu.austral.ingsis.clifford;

public class CommandParser {
  public Command<Directory> parse(String commandLine) {
    String[] parts = commandLine.split(" ", 2);
    String commandName = parts[0];
    String extra;
    if (parts.length > 1) {
      extra = parts[1];
    } else {
      extra = "";
    }

    switch (commandName) {
      case "ls":
        return parseLs(extra);
      case "cd":
        return new Cd(extra);
      case "mkdir":
        return new Mkdir(extra);
      case "touch":
        return new Touch(extra);
      case "pwd":
        return new Pwd();
      case "rm":
        return parseRm(extra);
      default:
        throw new IllegalArgumentException("Unknown command: " + commandName);
    }
  }

  private static Ls parseLs(String extra) {
    if (extra.startsWith("--ord=")) {
      return new Ls(extra.substring(6));
    } else {
      return new Ls();
    }
  }

  private static Rm parseRm(String extra) {
    if (extra.startsWith("--recursive")) {
      String[] parts2 = extra.split(" ", 2);
      String targetName;
      if (parts2.length > 1) {
        targetName = parts2[1];
      } else {
        targetName = "";
      }
      return new Rm(targetName, true);
    } else {
      return new Rm(extra);
    }
  }
}
