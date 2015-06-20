package net.springfieldusa.log;

import org.json.JSONArray;
import org.json.JSONException;

public interface LogService
{
  JSONArray getLogEntries(String query) throws JSONException;
}
