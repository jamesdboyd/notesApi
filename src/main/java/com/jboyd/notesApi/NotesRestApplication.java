package com.jboyd.notesApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NotesRestApplication 
{
   public static void main(String[] args)
   {
      @SuppressWarnings("unused")
      ApplicationContext context = SpringApplication.run(NotesRestApplication.class, args);
   }
}
