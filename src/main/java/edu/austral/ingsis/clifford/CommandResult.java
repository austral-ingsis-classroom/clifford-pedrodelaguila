package edu.austral.ingsis.clifford;


public record CommandResult<T extends FSNode>(T newRoot, T currentPosition, String output) {
  public static <T extends FSNode> CommandResult<T>
    noChange(T currentPosition, String output) {
         return new CommandResult<>(null, currentPosition, output);
  }
}
