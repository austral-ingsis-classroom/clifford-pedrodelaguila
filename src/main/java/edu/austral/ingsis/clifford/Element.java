package edu.austral.ingsis.clifford;

import java.util.List;

public interface Element {
  String getName();

  String getPath();

  boolean isDirectory();

  Element getParent();

  Element getRoot();

  boolean isLeaf();

  List<Element> getChildren();

  boolean isRoot();
}
