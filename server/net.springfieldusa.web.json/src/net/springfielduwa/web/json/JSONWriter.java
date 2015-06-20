package net.springfielduwa.web.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Component(service = JSONWriter.class)
public class JSONWriter implements MessageBodyWriter<JSONObject>
{
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
  {
    return JSONObject.class == type;
  }

  @Override
  public long getSize(JSONObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
  {
    return -1;
  }

  @Override
  public void writeTo(JSONObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException
  {
    entityStream.write(t.toString().getBytes());
  }

}
