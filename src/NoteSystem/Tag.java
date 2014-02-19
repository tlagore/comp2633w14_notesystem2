/**
 * 
 */
package NoteSystem;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Tag Entity class:
 * 		Holds information about connected Notes and the string value of the tag.
 *
 * @author	James C. Coté
 * @version v1.1 - Feb 18, 2014
 */
public class Tag extends SortableNoteTag
{
	// Private Variables
	private ArrayList< Note > m_AdjacentNotes;
	private String m_sValue;
	
	/**
	 * Default Constructor requires a value for the tag and a starting note.
	 * 
	 * @param sNewValue 		The String Value of the tag.
	 * @param nConnectedNote	The first Note that this tag represents.
	 */
	public Tag( String sNewValue, Note nConnectedNote )
	{
		m_sValue = sNewValue;
		m_AdjacentNotes = new ArrayList< Note >( );
		m_AdjacentNotes.add( nConnectedNote );
	}
	
	/**
	 * Overriding the inherited hashCode function for comparative purposes.
	 */
	@Override
	public int hashCode( )
	{
		int iReturnCode 	=  m_sValue.hashCode( ) +
							   m_AdjacentNotes.hashCode( );
		
		return iReturnCode;
	}
	
	/**
	 * Overloading Equals to properly compare 2 Tag Objects.
	 */
	@Override
	public boolean equals( Object obj )
	{
		// Local Variables
		boolean bReturnValue	= false;
		Tag eRHS				= null;
		
		if( null == obj )
			bReturnValue = false;
		else if( obj == this )
			bReturnValue = true;
		else if( !( obj.getClass( ) == this.getClass( ) ) )
			bReturnValue = false;
		else
		{
			eRHS = ( Tag )obj;
			bReturnValue = m_sValue.equals( eRHS.m_sValue ) &&
						   m_AdjacentNotes.equals( eRHS.m_AdjacentNotes );	
		}
		
		return bReturnValue;
	}
	
	/**
	 * Getters and Setters
	 */
	public String getTag( ) 				{ return m_sValue; }
	/*Shouldn't be Needed* 
	 * public void setTag( String sNewValue ) 	{ m_sValue = sNewValue; }
	 */
	
	/**
	 * Adds a new note to the adjacency list if the note doesn't already exist in the list.
	 * 
	 * @param nNewNote	The new Note to add.
	 */
	public void addNote( Note nNewNote )	
	{ 
		if( !m_AdjacentNotes.contains( nNewNote ) )
			m_AdjacentNotes.add( nNewNote ); 
	}
	
	/**
	 * Removes the specified note and returns true if the
	 * resulting adjacent Notes is an Empty List.
	 * 
	 * @param nNoteToRemove	Specified Note to Remove.
	 * @return				True if, after removing the specified Note,
	 * 						the resulting list of adjacent notes is empty.
	 */
	public boolean removeNote( Note nNoteToRemove )	
	{ 
		m_AdjacentNotes.remove( nNoteToRemove );
		return m_AdjacentNotes.isEmpty( );
	}

	/**
	 * Overridden Comparative object used for comparing 2 tags.
	 * Compares the 2 tag string values.  Returns 0 if they're both
	 * equal, a negative number if the left is lexicographically higher than
	 * the right and a positive number if the left is lower than the right.
	 */
	@Override
    public int compareTo( SortableNoteTag ntRHS )
    {
	    Tag tRHS = (Tag) ntRHS;
		return this.m_sValue.compareToIgnoreCase( tRHS.m_sValue );
    }

}
