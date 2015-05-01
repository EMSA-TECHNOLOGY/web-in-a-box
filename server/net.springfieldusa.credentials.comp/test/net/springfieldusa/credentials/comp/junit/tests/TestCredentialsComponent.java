package net.springfieldusa.credentials.comp.junit.tests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
  private DBCollection credentials;
  private DBCollection groups;
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
    credentials = mock(DBCollection.class);
    groups = mock(DBCollection.class);
    
    credentialsComponent = new CredentialsComponent();
    credentialsComponent.bindMongoDatabaseProvider(mongoDatabaseProvider);
    credentialsComponent.bindPasswordService(passwordService);
    
    when(mongoDatabaseProvider.getDB()).thenReturn(db);
    when(db.getCollection("credentials")).thenReturn(credentials);
    when(db.getCollection("groups")).thenReturn(groups);
  }
  
  @Test
  public void testAddCredential() throws EncryptionException
  {
    Credential credential = new Credential(email, password);
    
    when(passwordService.createSalt()).thenReturn(salt);
    when(passwordService.encryptPassword(password, salt)).thenReturn(encryptedPassword.getBytes());
    
    credentialsComponent.addCredential(credential );
    
    ArgumentCaptor<DBObject> argument = ArgumentCaptor.forClass(DBObject.class);
    verify(credentials).insert(argument.capture());
    assertThat(argument.getValue().get("password"), is(encryptedPassword.getBytes()));
  }
  
  @Test
  public void testAuthenticate() throws EncryptionException
  {
    DBObject query = new BasicDBObject("email", email );
    DBObject value = new BasicDBObject();
    value.put("password", encryptedPassword.getBytes());
    value.put("salt", salt);
    
    when(credentials.findOne(query)).thenReturn(value);
    when(passwordService.validatePassword(password, encryptedPassword.getBytes(), salt)).thenReturn(Boolean.TRUE);
    
    Principal principal = credentialsComponent.authenticate(new Credential(email, password));
    
    assertThat(principal, is(notNullValue()));
    assertThat(principal.getName(), is(email));
    
    assertThat(credentialsComponent.authenticate(new Credential(email, encryptedPassword)), is(nullValue()));
  }
  
  @Test
  public void testAuthorize()
  {
    Principal principal = mock(Principal.class);
    when(principal.getName()).thenReturn(email);
    
    DBObject query = new BasicDBObject(2);
    query.put("name", "admin");
    query.put("members", email);
    
    DBObject value = new BasicDBObject();
    when(groups.findOne(query)).thenReturn(value);
    
    assertTrue(credentialsComponent.authorize(principal, "admin"));
    assertFalse(credentialsComponent.authorize(principal, "root"));
  }
}