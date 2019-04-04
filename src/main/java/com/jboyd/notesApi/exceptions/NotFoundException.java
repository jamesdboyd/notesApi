package com.jboyd.notesApi.exceptions;

public class NotFoundException extends Exception
{
   public static final long serialVersionUID = 0L;

   public NotFoundException()
   {
      super();
   }
   public NotFoundException(String message)
   {
      super(message);
   }
}
