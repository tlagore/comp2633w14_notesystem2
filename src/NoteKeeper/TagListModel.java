package NoteKeeper;

import java.util.*;

import javax.swing.*;

import NoteSystem.*;

public class TagListModel extends AbstractListModel<Tag>
{
	private NoteSystem noteSystem;
	private ArrayList<Tag> tagList;
	
	public TagListModel(NoteSystem noteSystem)
	{
		this.noteSystem = noteSystem;
		tagList = noteSystem.getTagList();
	}
	
	public void fireChange()
	{
		noteSystem.quickSortTags();
		tagList = noteSystem.getTagList();
		
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	
	
	public void addTag(Tag tag)
	{
		
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	
	public void removeTag(Tag tag)
	{
	
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	
	@Override
	public Tag getElementAt(int el) 
	{
		Tag tag = null;
		
		if (el < tagList.size() && el >= 0)
			tag = tagList.get(el);
		
		return tag;		
	}

	@Override
	public int getSize() 
	{
		return tagList.size();
	}

	public void sortByTag(String tag) 
	{
		tagList = noteSystem.getTagList();
		ArrayList<Tag> temp = new ArrayList<Tag>();
		for (Tag t : tagList)
		{
			if (t.getTag().startsWith(tag))
				temp.add(t);
		}
		
		tagList = temp;
		fireContentsChanged(this, 0, tagList.size() - 1);
	}
	
	public void clearTagField()
	{
		tagList = noteSystem.getTagList();
		fireChange();
	}
}
