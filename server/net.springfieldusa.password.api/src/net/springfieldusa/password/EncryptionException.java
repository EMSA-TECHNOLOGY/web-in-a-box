package net.springfieldusa.password;

public class EncryptionException extends Exception
{
  private static final long serialVersionUID = 1L;

  public EncryptionException()
  {}

  public EncryptionException(String message)
  {
    super(message);
  }

  public EncryptionException(Throwable cause)
  {
    super(cause);
  }

  public EncryptionException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public EncryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
