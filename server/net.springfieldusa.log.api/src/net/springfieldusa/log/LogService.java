package net.springfieldusa.log;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

public interface LogService
{
  Collection<JSONObject> getLogEntries(String query) throws JSONException;
}
