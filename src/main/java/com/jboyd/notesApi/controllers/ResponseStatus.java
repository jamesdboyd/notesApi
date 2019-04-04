package com.jboyd.notesApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jboyd.notesApi.exceptions.NotFoundException;

/**
 * This is a wrapper class, that take any object T and is use to build the ResponseEntity we return it every response.
 * @author jboyd
 *
 * @param <T>
 */
public class ResponseStatus<T>
{
   private T type;
   private ResponseEntity<T> responseEntity;
   private boolean responseGood = true;
   private HttpStatus responseStatus = HttpStatus.OK;
   private Exception exception;
   
   public ResponseStatus() {}
   public ResponseStatus(T type) { this.type = type; }

   public T getType() { return type; }
   public void setType( T type ) { this.type = type; }
   public ResponseEntity<T> getResponseEntity() { return responseEntity; }
   public void setResponseEntity( ResponseEntity<T> re ) { this.responseEntity = re; }
   public boolean isResponseGood() { return responseGood; }
   public void setResponseGood( boolean requestOK ) { this.responseGood = requestOK; }
   public HttpStatus getResponseStatus() { return responseStatus; }
   public void setResponseStatus( HttpStatus responseStatus ) { this.responseStatus = responseStatus; }
   public Exception getException() { return exception; }
   public void setException( Exception exception ) 
   { 
      this.exception = exception; 
      responseGood = false;
      if( exception instanceof NotFoundException )
      {
         responseStatus = HttpStatus.NOT_FOUND;
      }
      else
      {
         // Server side problem
         responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      }
   }
   
   public boolean isGood() 
   {
      return responseGood;
   }
   
   ResponseEntity<?> buildResponse()
   {
      if( type != null && responseGood )
         return ResponseEntity.status(responseStatus).body(type);
      else
         return ResponseEntity.status(responseStatus).build();
   }
}
