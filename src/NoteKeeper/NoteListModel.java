package NoteKeeper;

import java.util.*;

import javax.swing.*;

import NoteSystem.*;

public class NoteListModel extends AbstractListModel<Note>
{
	private NoteSystem noteSystem;
	private ArrayList<Note> noteList;
	
	public NoteListModel(NoteSystem noteSystem)
	{
		this.noteSystem = noteSystem;
		noteList = noteSystem.getNoteList();
	}
	
	public Note loadNewNote()
	{
		Note newNote = null;
		
		newNote = noteSystem.loadNewNote();
		fireChange();
		
		return newNote;
	}
	
	public void updateNote(Note note)
	{
		noteSystem.updateNoteTagConnection(note);
		fireChange();
	}
	
	public void fireChange()
	{
		noteSystem.quickSortNotes();
		noteList = noteSystem.getNoteList();
		
		fireContentsChanged(this, 0, noteList.size() - 1);
	}
	
	@Override
	public Note getElementAt(int el) {
		Note note = null;
		
		if (el < noteList.size() && el >= 0)
			note = noteList.get(el);
		
		return note;
	}

	@Override
	public int getSize() {
		return noteList.size();
	}
	
	public void removeNote(Note note)
	{
		noteSystem.removeNote(note);
		fireChange();
	}

/*
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
 */
	
	
}
