package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public class UserRoot extends Directory {

  public UserRoot() {
    super(
        System.getProperty("user.name"), // nombre del user
        null, // sin parent
        new ArrayList<>(), // lista vacía de hijos
        null // root es null porque este mismo será el root
        );
    setRoot(this); // root se setea a sí mismo después de construir
  }

  @Override
  public boolean isRoot() {
    return true;
  }

  @Override
  public Directory getParent() {
    return null; // root no tiene padre
  }

  @Override
  public Directory getRoot() {
    return this; // root se referencia a sí misma
  }

  @Override
  public String getPath() {
    return "/" + getName();
  }
}
