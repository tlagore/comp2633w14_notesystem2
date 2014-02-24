/**
 * 
 */
package NoteKeeper;

import GUI.*;

import java.util.*;

import javax.swing.DefaultListModel;

import NoteSystem.*;

/**
 * NoteKeeper:
 * 	Takes the note list and tag list stored within the NoteSystem and puts it into a 
 * sorted ListModel in order to preserve an easily viewable list in the main window.
 *
 *	Handles viewing, adding, removing, and updating of individual notes between the 
 *client view and the NoteSystem.
 *
 * @author	Chris Gonzalez
 * @version v1.0 - Feb 19, 2014
 */

public class NoteKeeper
{
	private ArrayList<Note> noteList;
	private ArrayList<Tag> tagList;
	private NoteSystem noteSystem;
	
	private NoteSystemMainWindow mWindow;
	
	public NoteKeeper ()
	{
		noteSystem = new NoteSystem();
		noteList = noteSystem.getNoteList();
		tagList = noteSystem.getTagList();
		mWindow = new NoteSystemMainWindow(this, noteList.get(0));
	}
	
	public void run()
	{
		mWindow.loadNotes(noteList);
		mWindow.loadTags(tagList);
		mWindow.run();
	}
	
	public void reloadNotes()
	{
		mWindow.loadNotes(noteList);
	}
	
	public void reloadTags()
	{
		mWindow.loadTags(tagList);
	}
	
	public Note loadNote(int element)
	{
		return noteList.get(element);
	}
	
	public ArrayList<Note> notesByTag(String tag)
	{
		ArrayList<Note> toReturn = new ArrayList<Note>();
		for (Tag t : tagList)
		{
			if (t.getTag().startsWith(tag))
			{	
				for (Note n : t.getAdjacentNotes())
					toReturn.add(n);
			}
		}
		return noteSystem.quickSort(toReturn);
	}
	
	public ArrayList<Tag> tagsByTag(String tag)
	{
		ArrayList<Tag> toReturn = new ArrayList<Tag>();
		for (Tag t : tagList)
		{
			if (t.getTag().startsWith(tag))
				toReturn.add(t);
		}
		
		return noteSystem.quickSort(toReturn);
	}

	public void removeNote(Note note) 
	{
		noteSystem.removeNotes(note.getTitle());
		
	}

	public Note loadNewNote() 
	{
		return noteSystem.loadNewNote();
	}

	
	public void saveNote(String title, String desc, Note n)
	{
		// TODO Auto-generated method stub
		n.updateDate();
		n.setTitle(noteSystem.getUniqueTitle(title));
		n.setDesc(desc);
		
	}
	
	public int listSize()
	{
		return noteList.size();
	}
}