package com.jboyd.notesApi.model;

public class Note
{
   private int id;
   private String body;
   
   public Note() {}
   public Note(Integer id, String body) 
   {
      this.id = id;
      this.body = body;
   }

   public int getId() { return id; }
   public void setId( int id ) { this.id = id; }
   public String getBody() { return body; }
   public void setBody( String body ) { this.body = body; }
}
