package com.jboyd.notesApi;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jboyd.notesApi.model.Note;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotesRestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesControllerTest 
{
   @LocalServerPort
   private int port;
   
   private TestRestTemplate trt = new TestRestTemplate();
   
   @Test
   public void createNote() throws Exception
   {
      String body = "Unit Test CREATE Note XML";
      ResponseEntity<Note> result = createNote(body);
      
      // Did we get the correct response status code and does the Notes body contain the correct
      assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
      assertEquals(body, result.getBody().getBody());

      body = "Unit Test CREATE Note JSON";
      HttpHeaders headers = new HttpHeaders();
      headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
      headers.set("accept", MediaType.APPLICATION_JSON_VALUE);
      HttpEntity<String> request1 = new HttpEntity<>(asJsonString(new Note(0, body)), headers);
      ResponseEntity<String> result1 = trt.postForEntity(createUri("notes"), request1, String.class);

      // Did we get the correct response status code and does the Notes body contain the correct
      assertEquals(HttpStatus.CREATED.value(), result1.getStatusCodeValue());
      Note n = fromJsonToObject(result1.getBody(), Note.class);
      assertEquals(n.getBody(), body);
   }
   
   @Test
   public void getNote()
   {
      // Create new node first.
      String body = "Unit Test GET Note";
      ResponseEntity<Note> result = createNote(body);
      
      // Did we get the correct response status code and does the Notes body contain the correct
      assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
      assertEquals(body, result.getBody().getBody());
      Note n = result.getBody();

      ResponseEntity<Note> result1 = trt.getForEntity(createUri("notes/"+n.getId()), Note.class);
      assertEquals(HttpStatus.OK.value(), result1.getStatusCodeValue());
      Note n1 = result1.getBody();
      assertEquals(n.getId(), n1.getId());
   }
   
   @Test
   public void modifyNote()
   {
      // Create new node first.
      String body = "Unit Test CREATE Note";
      ResponseEntity<Note> result = createNote(body);
      
      // Did we get the correct response status code and does the Notes body contain the correct
      assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
      assertEquals(body, result.getBody().getBody());
      Note n = result.getBody();
      body = "Unit Test MODIFY Note";
      n.setBody(body);
      HttpHeaders headers = new HttpHeaders();
      headers.set("content-type", MediaType.APPLICATION_XML_VALUE);
      headers.set("accept", MediaType.APPLICATION_XML_VALUE);
      HttpEntity<Note> request = new HttpEntity<>(n, headers);
      result = trt.exchange(createUri("notes/"+n.getId()), HttpMethod.PUT, request, Note.class);

      ResponseEntity<Note> result1 = trt.getForEntity(createUri("notes/"+n.getId()), Note.class);
      assertEquals(HttpStatus.OK.value(), result1.getStatusCodeValue());
      Note n1 = result1.getBody();
      assertEquals(n.getId(), n1.getId());
   }
   
   @Test
   public void deleteNote()
   {
      // Create new node first.
      String body = "Unit Test DELETE Note";
      ResponseEntity<Note> result = createNote(body);
      
      // Did we get the correct response status code and does the Notes body contain the correct
      assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
      assertEquals(body, result.getBody().getBody());

      Note n = result.getBody();

      ResponseEntity<Note> result1 = trt.exchange(createUri("notes/"+n.getId()), HttpMethod.DELETE, null, Note.class);
      assertEquals(HttpStatus.NO_CONTENT.value(), result1.getStatusCodeValue());

      ResponseEntity<Note> result2 = trt.getForEntity(createUri("notes/"+n.getId()), Note.class);
      assertEquals(HttpStatus.NOT_FOUND.value(), result2.getStatusCodeValue());
   }
   
   @Test
   public void getAllNotes()
   {
      // Get all notes
      ResponseEntity<List<Note>> result = trt.exchange(createUri("notes/"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Note>>() {});
      assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
      
      List<Note> list = result.getBody();
      assertEquals(list.size(), 7);
   }
   
   private ResponseEntity<Note> createNote(String body)
   {
      HttpHeaders headers = new HttpHeaders();
      headers.set("content-type", MediaType.APPLICATION_XML_VALUE);
      headers.set("accept", MediaType.APPLICATION_XML_VALUE);
      HttpEntity<Note> request = new HttpEntity<>(new Note(0, body), headers);
      return trt.postForEntity(createUri("notes"), request, Note.class);
   }
   
   private String createUri(String api) { return "http://localhost:"+port+"/api/"+api; }
   
   private String asJsonString(final Object obj)
   {
      try
      {
         return new ObjectMapper().writeValueAsString(obj);
      }
      catch(Exception ex)
      {
         LogFactory.getLog(NotesControllerTest.class).error(ex.getMessage());
         throw new RuntimeException(ex);
      }
   }
   
   private <T> T fromJsonToObject(String json, Class<T> t)
   {
      try
      {
         return (T) new ObjectMapper().readValue(json, t);
      }
      catch(Exception ex)
      {
         LogFactory.getLog(NotesControllerTest.class).error(ex.getMessage());
         throw new RuntimeException(ex);
      }
   }
}