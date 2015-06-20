package net.springfieldusa.mongodb.comp;

import org.eclipselabs.emongo.MongoDatabaseProvider;

import com.mongodb.DBCollection;

import net.springfieldusa.comp.AbstractComponent;

public class MongoDBComponent extends AbstractComponent
{
  private volatile MongoDatabaseProvider databaseProvider;

  protected DBCollection getCollection(String name)
  {
    return databaseProvider.getDB().getCollection(name);
  }

  public void bindMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider)
  {
    this.databaseProvider = mongoDatabaseProvider;
  }
}
