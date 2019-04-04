package com.jboyd.notesApi.data;

import com.jboyd.notesApi.controllers.ResponseStatus;
import com.jboyd.notesApi.model.Note;

public interface DataStore
{
   /**
    * Create a new Note object.
    * @param id: The is of the Note.
    * @return: ResponseStatus containing the Note
    */
   public ResponseStatus<?> createNote(Note note);

   /**
    * Get a Note object with the requested id.
    * @param id: The is of the Note.
    * @return: ResponseStatus containing the Note
    */
   public ResponseStatus<?> getNote(int id);
   
   /**
    * Update an existing Note object.
    * @param id: The Note to update, the body of the note will be updated.
    * @return: ResponseStatus containing the Note that we updated
    */
   public ResponseStatus<?> updateNote(Note note); 
   
   /**
    * Delete an existing Note object.
    * @param id: The Note to delete.
    * @return: ResponseStatus with empty body
    */
   public ResponseStatus<?> deleteNote(int id);
   
   /**
    * Get all Notes, or just get Notes based on the search criteria. 
    * @param sc: The search criteria which can be NULL.
    * @return: ResponseStatus containing a List of Notes. The List can be empty.
    */
   public ResponseStatus<?> getNotes(SearchCriteria sc);
}
