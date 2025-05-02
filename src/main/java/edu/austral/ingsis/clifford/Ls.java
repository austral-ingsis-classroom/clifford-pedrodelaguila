package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Ls implements Command<Directory> {
  private final String sortDirection;

  public Ls() {
    this("");
  }

  public Ls(String sortDirection) {
    this.sortDirection = sortDirection;
  }

  @Override
  public CommandResult<Directory> execute(Directory directory) {
    List<FSNode> FSNodes = directory.listNodes();

    if (FSNodes.isEmpty()) {
      return CommandResult.noChange(directory, "");
    }

    List<FSNode> sortedFSNodes = new ArrayList<>(FSNodes);

    if (sortDirection.equals("asc")) {
      sortedFSNodes.sort(Comparator.comparing(FSNode::getName));
    } else if (sortDirection.equals("desc")) {
      sortedFSNodes.sort(Comparator.comparing(FSNode::getName).reversed());
    }

    List<String> names = new ArrayList<>();
    for (FSNode e : sortedFSNodes) {
      names.add(e.getName());
    }

    return CommandResult.noChange(directory, String.join(" ", names));
  }
}
