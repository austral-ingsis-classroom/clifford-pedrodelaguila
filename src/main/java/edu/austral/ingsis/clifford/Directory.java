package edu.austral.ingsis.clifford;

import java.util.*;

public record Directory(String name, Directory parent, Map<String, FSNode> children)
        implements FSNode {

  public Directory {
    children = Collections.unmodifiableMap(new LinkedHashMap<>(children));
  }

  public Directory(String name, Directory parent) {
    this(name, parent, new LinkedHashMap<>());
  }

  public Directory() {
    this("/", null, new LinkedHashMap<>());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Category getCategory() {
    return Category.DIRECTORY;
  }

  @Override
  public String getLocation() {
    if (parent == null) {
      return "/";
    }
    if (parent.parent == null) {
      return "/" + name;
    }
    return parent.getLocation() + "/" + name;
  }

  public FSNode findNode(String nodeName) {
    return children.get(nodeName);
  }

  public List<FSNode> listNodes() {
    return new ArrayList<>(children.values());
  }

  public Directory insertNode(FSNode FSNode) {
    LinkedHashMap<String, FSNode> newChildren = new LinkedHashMap<>(children);
    newChildren.put(FSNode.getName(), FSNode);
    return new Directory(name, parent, newChildren);
  }

  public Directory deleteNode(String nodeName) {
    LinkedHashMap<String, FSNode> newChildren = new LinkedHashMap<>(children);
    newChildren.remove(nodeName);
    return new Directory(name, parent, newChildren);
  }
}
