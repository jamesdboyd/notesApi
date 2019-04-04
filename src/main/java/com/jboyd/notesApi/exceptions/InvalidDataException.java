package com.jboyd.notesApi.exceptions;

public class InvalidDataException extends Exception
{
   public static final long serialVersionUID = 0L;

   public InvalidDataException()
   {
      super();
   }
   public InvalidDataException(String message)
   {
      super(message);
   }
}
