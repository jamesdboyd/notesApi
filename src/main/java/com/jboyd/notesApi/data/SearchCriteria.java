package com.jboyd.notesApi.data;

/**
 * This class is used to hold query parameters from http client requests
 * @author jboyd
 *
 */
public class SearchCriteria
{
   private String pattern;

   public SearchCriteria() {}
   public SearchCriteria(String pattern) { this.pattern = pattern; }

   public String getPattern() { return pattern; }
   public void setPattern( String pattern ) { this.pattern = pattern; }
}
