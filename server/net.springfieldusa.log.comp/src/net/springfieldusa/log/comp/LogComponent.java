package net.springfieldusa.log.comp;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.log.LogService;
import net.springfieldusa.storage.StorageService;

@Component(service = LogService.class)
public class LogComponent extends AbstractComponent implements LogService
{
  private String logCollection;
  private volatile StorageService storageService;
  
  @Activate
  public void activate(Map<String, Object> properties)
  {
    logCollection = (String) properties.get("collection");
    
    if(logCollection == null)
      logCollection = "logs";
  }
  
  @Override
  public JSONArray getLogEntries(String query) throws JSONException
  {
    return storageService.find(logCollection, query);
  }
  
  @Reference(unbind = "-")
  public void bindStorageService(StorageService storageService)
  {
    this.storageService = storageService;
  }
}
