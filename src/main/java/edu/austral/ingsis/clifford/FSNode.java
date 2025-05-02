package edu.austral.ingsis.clifford;

public sealed interface FSNode permits Directory, File {
  String getName();

  Category getCategory();

  String getLocation();
}
