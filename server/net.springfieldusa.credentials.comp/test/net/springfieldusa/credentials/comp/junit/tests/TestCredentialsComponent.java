package net.springfieldusa.credentials.comp.junit.tests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.comp.CredentialsComponent;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;

import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class TestCredentialsComponent
{
  private CredentialsComponent credentialsComponent;
  private PasswordService passwordService;
  private MongoDatabaseProvider mongoDatabaseProvider;
  private DB db;
  private DBCollection collection;
  private String email;
  private String password;
  private String encryptedPassword;
  private byte[] salt;
  
  @Before
  public void setUp()
  {
    email = "nobody@nowhere.org";
    password = "password";
    salt = new byte[] {0, 1, 2};
    encryptedPassword = "encryptedPassword";
    passwordService = mock(PasswordService.class);
    mongoDatabaseProvider = mock(MongoDatabaseProvider.class);
    db = mock(DB.class);
    collection = mock(DBCollection.class);
    
    credentialsComponent = new CredentialsComponent();
    credentialsComponent.bindMongoDatabaseProvider(mongoDatabaseProvider);
    credentialsComponent.bindPasswordService(passwordService);
    
    when(mongoDatabaseProvider.getDB()).thenReturn(db);
    when(db.getCollection("credentials")).thenReturn(collection);
  }
  
  @Test
  public void testAddCredential() throws EncryptionException
  {
    Credential credential = new Credential(email, password);
    
    when(passwordService.createSalt()).thenReturn(salt);
    when(passwordService.encryptPassword(password, salt)).thenReturn(encryptedPassword.getBytes());
    
    credentialsComponent.addCredential(credential );
    
    ArgumentCaptor<DBObject> argument = ArgumentCaptor.forClass(DBObject.class);
    verify(collection).insert(argument.capture());
    assertThat(argument.getValue().get("password"), is(encryptedPassword.getBytes()));
  }
  
  @Test
  public void testAuthenticate() throws EncryptionException
  {
    DBObject query = new BasicDBObject("email", email );
    DBObject value = new BasicDBObject();
    value.put("password", encryptedPassword.getBytes());
    value.put("salt", salt);
    
    when(collection.findOne(query)).thenReturn(value);
    when(passwordService.validatePassword(password, encryptedPassword.getBytes(), salt)).thenReturn(Boolean.TRUE);
    
    Principal principal = credentialsComponent.authenticate(email, password);
    
    assertThat(principal, is(notNullValue()));
    assertThat(principal.getName(), is(email));
    
    assertThat(credentialsComponent.authenticate(email, encryptedPassword), is(nullValue()));
  }
}