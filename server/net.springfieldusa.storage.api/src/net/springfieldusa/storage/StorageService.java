package net.springfieldusa.storage;

public interface StorageService
{
  void create(String collectionName, String json);
  String retrieve(String collectionName, String id);
  void update(String collectionName, String json);
  void delete(String collectionName, String id);
  String find(String collectionName, String query);
}
