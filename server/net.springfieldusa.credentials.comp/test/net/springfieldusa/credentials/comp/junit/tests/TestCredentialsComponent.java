package net.springfieldusa.credentials.comp.junit.tests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.credentials.CredentialException;
import net.springfieldusa.credentials.comp.CredentialsComponent;
import net.springfieldusa.groups.GroupsService;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.storage.StorageService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class TestCredentialsComponent
{
  private CredentialsComponent credentialsComponent;
  private PasswordService passwordService;
  private StorageService storageService;
  private GroupsService groupsService;
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
    storageService = mock(StorageService.class);
    groupsService = mock(GroupsService.class);
    
    credentialsComponent = new CredentialsComponent();
    credentialsComponent.bindStorageService(storageService);
    credentialsComponent.bindPasswordService(passwordService);    
    credentialsComponent.bindGroupsService(groupsService);
  }
  
  @Test
  public void testAddCredential() throws EncryptionException, CredentialException, JSONException
  {
    Credential credential = new Credential(email, password);
    
    when(passwordService.createSalt()).thenReturn(salt);
    when(passwordService.encryptPassword(password, salt)).thenReturn(encryptedPassword.getBytes());
    
    credentialsComponent.addCredential(credential);
    
    ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
    verify(storageService).create(eq("credentials"), argument.capture());
    assertThat(argument.getValue().get("password"), is(encryptedPassword.getBytes()));
  }
  
  @Test
  public void testAuthenticate() throws EncryptionException, CredentialException, JSONException
  {
    JSONObject value = new JSONObject();
    value.put("password", new JSONArray(encryptedPassword.getBytes()));
    value.put("salt", new JSONArray(salt));
    
    when(storageService.retrieve("credentials", "userId", email)).thenReturn(value);
    when(passwordService.validatePassword(password, encryptedPassword.getBytes(), salt)).thenReturn(Boolean.TRUE);
    
    Principal principal = credentialsComponent.authenticate(new Credential(email, password));
    
    assertThat(principal, is(notNullValue()));
    assertThat(principal.getName(), is(email));
    
    assertThat(credentialsComponent.authenticate(new Credential(email, encryptedPassword)), is(nullValue()));
  }
  
  @Test
  public void testAuthorize() throws CredentialException, JSONException
  {
    Principal principal = mock(Principal.class);
    when(principal.getName()).thenReturn(email);
    
    Set<String> value = new HashSet<>();
    value.add(email);
    when(groupsService.getUsersInGroup("admin")).thenReturn(value);
    when(groupsService.getUsersInGroup("root")).thenReturn(Collections.emptySet());
    
    assertTrue(credentialsComponent.authorize(principal, "admin"));
    assertFalse(credentialsComponent.authorize(principal, "root"));
  }
}