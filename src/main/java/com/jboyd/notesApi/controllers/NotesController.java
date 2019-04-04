package com.jboyd.notesApi.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jboyd.notesApi.data.HashDataStore;
import com.jboyd.notesApi.data.SearchCriteria;
import com.jboyd.notesApi.exceptions.InvalidDataException;
import com.jboyd.notesApi.model.Note;

import io.swagger.annotations.ApiOperation;

@RestController
public class NotesController
{
   Log log = LogFactory.getLog(NotesController.class);
   
   @ApiOperation(
         value = "Create a new Note.",
         notes = "The new Note is returned. The id field of the new Note will be ignored.",
         consumes = "application/json or application/xml",
         response = Note.class,
         produces = "application/json or application/xml")
   @RequestMapping( value = "/notes",
   method = RequestMethod.POST, 
   consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
   produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
   ResponseEntity<?> createNote( @RequestBody Note note  )
   {
      HashDataStore hds = new HashDataStore();
      return hds.createNote(note).buildResponse();
   }
   
   @ApiOperation(
         value = "Retrieve a Note whose id is given as the path template {id}",
         notes = "The corresponding Note is returned. If the Note does not exist, nothing is returned and a 404 HTTP status is reported.",
         response = Note.class,
         produces = "application/json or application/xml")
   @RequestMapping( value = "/notes/{id}", 
   method = RequestMethod.GET, 
   produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
   ResponseEntity<?> getNote( @PathVariable String id )
   {
      HashDataStore hds = new HashDataStore();
      int ID = 0;
      try
      {
         ID = Integer.parseInt(id);
      }
      catch(NumberFormatException ex)
      {
         log.error("Invalid ID requested "+ID);
         ResponseStatus<Note> rs = new ResponseStatus<>();
         rs.setResponseGood(false);
         rs.setResponseStatus(HttpStatus.BAD_REQUEST);
         return rs.buildResponse();
      }
      return hds.getNote(ID).buildResponse();
   }
   
   @ApiOperation(
         value = "Update a Note whose id is given as the path template {id}",
         notes = "The updated Note is returned. If the Note does not exist, nothing is returned and a 404 HTTP status is reported.",
         consumes = "application/json or application/xml",
         response = Note.class,
         produces = "application/json or application/xml")
   @RequestMapping( value = "/notes/{id}",
   method = RequestMethod.PUT, 
   consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
   produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
   ResponseEntity<?> updateNote( @PathVariable String id, @RequestBody Note note  )
   {
      int ID = 0;
      HashDataStore hds = new HashDataStore();
      try
      {
         ID = Integer.parseInt(id);
         if( note.getId() != ID )
            throw new InvalidDataException();
      }
      catch(NumberFormatException | InvalidDataException ex)
      {
         String s = "Invalid ID requested "+ID;
         if( ex instanceof InvalidDataException )
            s = "The path id "+ID+" does not match the Note id "+note.getId();

         log.error(s);
         ResponseStatus<Note> rs = new ResponseStatus<>();
         rs.setResponseGood(false);
         rs.setResponseStatus(HttpStatus.BAD_REQUEST);
         return rs.buildResponse();
      }
      return hds.updateNote(note).buildResponse();
   }
   
   @ApiOperation(
         value = "Delete a Note whose id is given as the path template {id}",
         notes = "If the Note does not exist, nothing is returned and a 404 HTTP status is reported.")
   @RequestMapping( value = "/notes/{id}", method = RequestMethod.DELETE)
   ResponseEntity<?> deleteNote(@PathVariable String id)
   {
      int ID = 0;
      HashDataStore hds = new HashDataStore();
      try
      {
         ID = Integer.parseInt(id);
      }
      catch(NumberFormatException ex)
      {
         String s = "Invalid ID requested "+ID;
         log.error(s);
         ResponseStatus<Note> rs = new ResponseStatus<>();
         rs.setResponseGood(false);
         rs.setResponseStatus(HttpStatus.BAD_REQUEST);
         return rs.buildResponse();
      }
      return hds.deleteNote(ID).buildResponse();
   }


   @ApiOperation( 
         value = "Get all notes, returns a list of Note objects.",
         consumes = "application/json or application/xml",
         produces = "application/json or application/xml")
   @RequestMapping( value = "/notes", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
   ResponseEntity<?> getNotes(@RequestParam(value = "query", defaultValue = "") String query)
   {
      HashDataStore hds = new HashDataStore();
      SearchCriteria sc = null;
      if( query != null && ! query.isEmpty() )
      {
         sc = new SearchCriteria(query);
      }
      return hds.getNotes(sc).buildResponse();
   }
}
