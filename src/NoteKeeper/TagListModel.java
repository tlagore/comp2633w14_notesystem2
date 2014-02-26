package NoteKeeper;

import java.util.*;

import javax.swing.*;

import NoteSystem.*;
/**
 * Handles the interchange between the NoteSystemMainWindow and the actual note system to ensure
 * that the main window has no access to the actual list.
 * 
 * @version February 25, 2014
 * @author Chris Gonzalez
 */

public class TagListModel extends AbstractListModel<Tag>
{
	private NoteSystem noteSystem;
	private ArrayList<Tag> tagList;
	
	public TagListModel(NoteSystem noteSystem)
	{
		this.noteSystem = noteSystem;
		tagList = noteSystem.getTagList();
	}
	
	/**
	 * Informs the connected lists that a change has occurred and visuals must
	 * 			be updated.
	 */
	public void fireChange()
	{
		noteSystem.quickSortTags();
		tagList = noteSystem.getTagList();
		
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	
	/**
	 * Returns an element at a requested position
	 * @return tag The element at the position
	 */
	@Override
	public Tag getElementAt(int el) 
	{
		Tag tag = null;
		
		if (el < tagList.size() && el >= 0)
			tag = tagList.get(el);
		
		return tag;		
	}

	/**
	 * Override for the getSize of an AbstractListModel.  Returns the size of the tagList
	 */
	@Override
	public int getSize() 
	{
		return tagList.size();
	}

	/**
	 * Sorts the tag list by a specified tag and updates the view
	 * @param tag the tag the list is being sorted by
	 */
	public void sortByTag(String tag) 
	{
		tagList = noteSystem.getFilteredTags(tag);
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	/**
	 * Used when the a tag has been selected and a clear has been requested
	 */
	public void clearTagField()
	{
		tagList = noteSystem.getTagList();
		fireChange();
	}
}
