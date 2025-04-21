package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Element {

  private final String name;
  private Element parent;
  private Element root;
  private List<Element> children;

  public Directory(String name, Element parent, List<Element> children, Element root) {
    this.name = name;
    this.parent = parent;
    this.children = new ArrayList<>();
    this.root = root;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setRoot(Element root) {
    this.root = root;
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
    if (children.isEmpty()) {
      return true;
    }
    ;
    return false;
  }

  public List<Element> getChildren() {
    return children;
  }

  @Override
  public boolean isRoot() {
    return false;
  }

  public void removeChild(Element child) {
    this.children.remove(child);
  }

  public void removeAllChildren() {
    this.children.clear();
  }

  public void addChild(Element child) {
    if (child instanceof UserRoot) {
      throw new IllegalArgumentException("Child cannot be root");
    }
    // Check if the child is not null and not already in the list
    if (child != null && !this.children.contains(child)) {
      this.children.add(child);
    }
  }
}
