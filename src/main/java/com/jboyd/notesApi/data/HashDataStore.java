package com.jboyd.notesApi.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.jboyd.notesApi.controllers.ResponseStatus;
import com.jboyd.notesApi.exceptions.NotFoundException;
import com.jboyd.notesApi.model.Note;

/**
 * This class implement DataStore and uses a Map as the data store.
 * @author jboyd
 *
 */
public class HashDataStore implements DataStore
{
   private static final String DATA_FILE_NAME = "notes.txt";
   public static Map<Integer, Note> dataMap = new TreeMap<>();
   static { initializeDataStore(); }
   private Log log = LogFactory.getLog(HashDataStore.class);

   public HashDataStore() {}

   @Override
   public ResponseStatus<?> createNote(Note note)
   {
      int lastId = ((TreeMap<Integer, Note>)dataMap).lastKey();
      dataMap.put(++lastId, note);
      note.setId(lastId);
      ResponseStatus<Note> rs = new ResponseStatus<Note>(note);
      rs.setResponseStatus(HttpStatus.CREATED);
      return rs;
   }
   
   @Override
   public ResponseStatus<Note> getNote(int id)
   {
      ResponseStatus<Note> rs = new ResponseStatus<>();
      Note note = null;
      synchronized(dataMap)
      {
         note = dataMap.get(id);
      }
      if( note == null )
      {
         String s = "Could not find note with id: "+id;
         log.error(s);
         rs.setException(new NotFoundException(s));
      }
      rs.setType(note);
      return rs;
   }

   @Override
   public ResponseStatus<?> updateNote(Note note) 
   {
      Note rn = null;
      ResponseStatus<Note> rs = new ResponseStatus<>();
      synchronized(dataMap)
      {
         rn = dataMap.get(note.getId());
         if( rn == null )
         {
            String s = "Could not find note with id: "+note.getId();
            log.error(s);
            rs.setException(new NotFoundException(s));
         }
         else
            rn.setBody(note.getBody());
      }
      rs.setType(rn);
      return rs;
   }

   @Override
   public ResponseStatus<Note> deleteNote(int id)
   {
      ResponseStatus<Note> rs = new ResponseStatus<>();
      rs.setResponseStatus(HttpStatus.NO_CONTENT);
      Note note = null;
      synchronized(dataMap)
      {
         note = dataMap.get(id);
         if( note == null )
         {
            String s = "Could not find note with id: "+id;
            log.error(s);
            rs.setException(new NotFoundException(s));
            rs.setResponseStatus(HttpStatus.NOT_FOUND);
         }
         else
         {
            dataMap.remove(note.getId());
         }
      }
      return rs;
   }

   @Override
   public ResponseStatus<?> getNotes(SearchCriteria sc)
   {
      List<Note> list = new ArrayList<>();
      for(Map.Entry<Integer, Note> entry: dataMap.entrySet() )
      {
         if( sc != null )
         {
            if( sc.getPattern() == null || sc.getPattern().isEmpty() )
            {
               list.add(entry.getValue());
            }
            else
            {
               if( entry.getValue().getBody().contains(sc.getPattern()) )
                  list.add(entry.getValue());
            }
         }
         else
            list.add(entry.getValue());
      }
      return new ResponseStatus<List<Note>>(list);
   }
   
   /***
    * Read in the file DATA_FILE_NAME to populate/initialize the data store
    */
   private static void initializeDataStore()
   {
      try
      {
         File f = new File(DATA_FILE_NAME);
         if( ! f.exists() )
         {
            String s = "The file "+DATA_FILE_NAME+" does not exist.";
            LogFactory.getLog(HashDataStore.class).error(s);
            throw new NotFoundException(s);
         }
         try( BufferedReader in = new BufferedReader(new FileReader(f)) )
         {
            String line;
            Pattern p = Pattern.compile("^(\\d+)\\s+(.*)$");
            while ( (line = in.readLine()) != null )
            {
               Matcher m = p.matcher(line);
               if( m.matches() )
               {
                  Integer i = Integer.parseInt(m.group(1));
                  Note n = new Note(i, m.group(2));
                  dataMap.put(i, n);
               }
            }
         }
      }
      catch(NotFoundException | IOException ex)
      {
         LogFactory.getLog(HashDataStore.class).error(ex.getMessage(), ex);
      }
   }
}