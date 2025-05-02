package edu.austral.ingsis.clifford;

public record File(String name, Directory parent, String content) implements FSNode {
  @Override
  public String getName() {
    return name;
  }

  @Override
  public Category getCategory() {
    return Category.FILE;
  }

  @Override
  public String getLocation() {
    String parentLocation = parent.getLocation();
    if ("/".equals(parentLocation)) {
      return parentLocation + name;
    } else {
      return parentLocation + "/" + name;
    }
  }
}
