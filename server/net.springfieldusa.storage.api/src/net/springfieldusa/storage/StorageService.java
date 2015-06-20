package net.springfieldusa.storage;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

public interface StorageService
{
  JSONObject create(String collection, JSONObject json) throws JSONException;
  JSONObject retrieve(String collection, String id) throws JSONException;
  JSONObject retrieve(String collection, String key, String value) throws JSONException;
  void update(String collection, String JSONObject);
  void delete(String collection, String id);
  Collection<JSONObject> find(String collection, String query) throws JSONException;
}
