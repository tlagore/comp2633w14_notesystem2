/**
 * 
 */
package NoteKeeper;

import gui.MainWindow;

import java.util.*;

import javax.swing.*;

import systemCode.Book;
import GUI.NoteSystemMainWindow;
import NoteSystem.*;

/**
 * NoteKeeper:
 * 	Takes the note list and tag list stored within the NoteSystem and puts it into a 
 * sorted ListModel in order to preserve an easily viewable list in the main window.
 *
 *	Handles viewing, adding, removing, and updating of individual notes between the 
 *client view and the NoteSystem.
 *
 * @author	Tyrone Lagore
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
		
		return toReturn;
	}
	
	public ArrayList<Tag> tagsByTag(String tag)
	{
		ArrayList<Tag> toReturn = new ArrayList<Tag>();
		for (Tag t : tagList)
		{
			if (t.getTag().startsWith(tag))
				toReturn.add(t);
		}
		
		return toReturn;
	}
}