package edu.austral.ingsis.clifford;

public sealed interface Command<T extends FSNode> permits Cd, Ls, Mkdir, Pwd, Touch, Rm {
  CommandResult<T> execute(T node);
}
