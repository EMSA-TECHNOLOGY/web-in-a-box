package net.springfieldusa.email;

public interface EmailService
{
  void sendMessage(String email, String Subject, String message);
}
