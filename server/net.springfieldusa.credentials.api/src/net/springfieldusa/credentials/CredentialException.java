package net.springfieldusa.credentials;

public class CredentialException extends Exception
{
  private static final long serialVersionUID = 1647005055356130708L;

  public CredentialException()
  {
  }

  public CredentialException(String message)
  {
    super(message);
  }

  public CredentialException(Throwable cause)
  {
    super(cause);
  }

  public CredentialException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public CredentialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
