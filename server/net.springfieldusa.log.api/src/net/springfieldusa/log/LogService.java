package net.springfieldusa.log;

import java.util.List;

import org.json.JSONObject;

public interface LogService
{
  List<JSONObject> getLogEntries(String query);
}
