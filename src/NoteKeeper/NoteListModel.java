package NoteKeeper;

import java.util.*;

import javax.swing.*;

import NoteSystem.*;
/**
 * Handles the note list for display in the main window.  Acts as a medium between
 * the main window and the note system guaranteeing that the window does not have 
 * direct access to the list.
 * 
 * @version February 25, 2014
 * @author Tyrone Lagore
 */
public class NoteListModel extends AbstractListModel<Note>
{
	private NoteSystem noteSystem;
	private ArrayList<Note> noteList;
	
	public NoteListModel(NoteSystem noteSystem)
	{
		this.noteSystem = noteSystem;
		noteList = noteSystem.getNoteList();
	}
	
	/**
	 * Name: loadNewNote
	 * Purpose: Requests the system to load a new note and give reference to that note.
	 * 
	 * @return newNote - The note being created
	 */
	public Note loadNewNote(List<Tag> selectedTags)
	{
		Note newNote = null;
		
		newNote = noteSystem.loadNewNote();
		
		if (selectedTags.size() > 0)
			for (Tag t : selectedTags)
				newNote.addTag(t.getTag());
		
		fireChange();
		
		return newNote;
	}
	
	/**
	 * Name: updateNote
	 * Purpose: Updates a changed note in the system by finding a unique title for the note
	 * 			then requesting an update of the note and tag connections to ensure it is properly
	 * 			linked into the system.
	 * 
	 * @param note the note that has changed that is being updated within the system
	 * @param title the requested title (may change if it is taken)
	 * @param titleChanged whether or not the title was edited
	 */
	public void updateNote(Note note, String title, String oldTitle)
	{
		if (oldTitle.compareTo(title) != 0)
			note.setTitle(noteSystem.getUniqueTitle(title));
		
		noteSystem.updateNoteTagConnection(note);
		fireChange();
	}
	
	/**
	 * Name: fireChange
	 * Purpose: Sorts the list of notes, attains the reference to the list and then informs
	 * 			the JList connected to the list model that it's contents have changed and visual
	 *  		must be updated.
	 */
	public void fireChange()
	{
		noteSystem.quickSortNotes();
		noteList = noteSystem.getNoteList();
		
		fireContentsChanged(this, 0, noteList.size() - 1);
	}
	
	/**
	 * Name getElementAt
	 * Purpose: Returns an element at the specified index.
	 * @return note The note at the index
	 */
	@Override
	public Note getElementAt(int el) {
		Note note = null;
		
		if (el < noteList.size() && el >= 0)
			note = noteList.get(el);
		
		return note;
	}
	
	/**
	 * Name: getSize
	 * Purpose: Returns the size of the list
	 * 
	 * @param int the size of the list
	 */
	@Override
	public int getSize() {
		return noteList.size();
	}
	
	/**
	 * Removes a note from the system
	 * @param note The note being removed
	 */
	public void removeNote(Note note)
	{
		noteSystem.removeNote(note);
		fireChange();
	}

	/**
	 * Name: sortByTag
	 * Purpose: Sorts the notes by a specified tag.
	 * 
	 * @param tag the tag to sort the notes by
	 */
	public void sortByTag(String tag)
	{
		noteList = noteSystem.getFilteredNotes(tag);
		fireContentsChanged(this, 0, noteList.size() - 1);
	}
	
	/**
	 * Name: clearTagField
	 * Purpose: Used to reset the note list back to the full list after a sort by 
	 * 			tag has completed.
	 */
	public void clearTagField()
	{
		noteList = noteSystem.getNoteList();
		fireChange();
	}
}
