package edu.austral.ingsis.clifford;

import java.util.List;

public class File implements Element {
  String name;
  Element parent;
  Element root;

  public File(String name, Element parent, Element root) {
    this.name = name;
    this.parent = parent;
    this.root = root;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    String path = "";
    Element current = this;

    while (current != null && current != root) {
      path = "/" + current.getName() + path;
      current = current.getParent();
    }

    return root.getPath() + path;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  public Element getParent() {
    return parent;
  }

  public Element getRoot() {
    return root;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public List<Element> getChildren() {
    return List.of();
  }

  @Override
  public boolean isRoot() {
    return false;
  }
}
