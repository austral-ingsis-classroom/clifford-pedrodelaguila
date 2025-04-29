package edu.austral.ingsis.clifford;

import java.util.Collections;
import java.util.List;

public class Directory implements Element {

  private final String name;
  private final Element parent;
  private final Element root;
  private final List<Element> children;

  public Directory(String name, Element parent, List<Element> children, Element root) {
    this.name = name;
    this.parent = parent;
    this.root = root;
    this.children = Collections.unmodifiableList(children);
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
    return true;
  }

  public Element getParent() {
    return parent;
  }

  public Element getRoot() {
    return root;
  }

  @Override
  public boolean isLeaf() {
    return children.isEmpty();
  }

  public List<Element> getChildren() {
    return children;
  }
}
