/**
 * 
 */
package NoteSystem;

import java.io.*;
import java.util.*;
import GUI.NoteSystemMainWindow;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Main System for Managing Notes and Tags.
 *
 * @author	James Coté
 * @version v1.1 - Feb 18, 2014
 */
public class NoteSystem 
{
	// Private Variables
	private ArrayList< Note > m_NotesList;
	private ArrayList< Tag > m_TagsList;
	private NoteSystemMainWindow m_Window;
	
	// Constants
	private static final String s_FileName = "Notes.xml";
	private static final String s_DefaultTitle = "(New Note)";
	
	
	/**
	 * Default Constructor.  Loads the list from the Notes.txt file.
	 */
	public NoteSystem( )
	{
		m_NotesList = new ArrayList< Note >( );
		m_TagsList = new ArrayList< Tag >( );
		loadList( );
		
		m_Window = new NoteSystemMainWindow( this, m_NotesList.get( 0 ) );
		m_Window.run( );
	}
	
	/**
	 * Getters for the Note and Tag lists.
	 */
	public ArrayList< Note > getNoteList( )		{ return m_NotesList; }
	public ArrayList< Tag > getTagList( )		{ return m_TagsList; }
	
	/**
	 * Loads a list from the XML file and populates the Tag List.
	 */
	private void loadList( )
	{
		String sInput = "";
        try
        {
            XStream xstream = new XStream(new StaxDriver() );
            Scanner input = new Scanner( new File( s_FileName ) );
            
            while( input.hasNext( ) )
            	sInput += input.nextLine( ) + "\n";
            
            m_NotesList = ( ArrayList< Note > ) xstream.fromXML( sInput );
            
            populateTagsList( );   

        }
        catch(Exception ex) {} 
        
        if( m_NotesList.isEmpty( ) )
        	loadNewNote( );
	}
	
	/**
	 * Populates the Tag list based on the current Note List.
	 */
	private  void populateTagsList( )
	{
		for( Note nIndex : m_NotesList )
		{
			for( String sIndex : nIndex.getTags( ) )
			{
				if( m_TagsList.contains( sIndex ) )
					m_TagsList.get( m_TagsList.indexOf( sIndex ) ).addNote( nIndex );
				else
					m_TagsList.add( new Tag( sIndex, nIndex ) );
			}
		}
	}
	
	/**
	 * Internally updates Tag Connections to a particular Note.
	 * 
	 * @param nNoteToUpdate	The Note to cross-check connections to.
	 */
	public void updateNoteTagConnection( Note nNoteToUpdate )
	{
		ArrayList< Tag > m_ReferencedTags 	= getLinkedTags( nNoteToUpdate );
		ArrayList< String > m_NoteTags		= nNoteToUpdate.getTags( );
		Tag m_ReferenceTag = null;
		
		for( Tag tIndex : m_ReferencedTags )
		{
			if( !m_NoteTags.contains( tIndex.getTag( ) ) )
			{
				tIndex.removeNote( nNoteToUpdate );
				if( tIndex.getAdjacentNotes( ).isEmpty( ) )
					m_TagsList.remove( tIndex );
			}
			else
				m_NoteTags.remove( tIndex.getTag( ) );
		}
		
		
		for( String sIndex : m_NoteTags )
		{
			m_ReferenceTag = getTagByValue( sIndex );
			if( m_ReferenceTag != null )
				m_ReferenceTag.addNote( nNoteToUpdate );
			else
				m_TagsList.add( new Tag( sIndex, nNoteToUpdate ) );
		}
	}
	
	/**
	 * Given a string value, this function will return the tag that matches the value.
	 * It will return null if no tag could be found.
	 * 
	 * @param sValue	The String Value of the Tag to find.
	 * @return			Null if a valid tag could not be found.  Otherwise, returns the 
	 * 					desired tag.
	 */
	private Tag getTagByValue( String sValue )
	{		
		for( Tag nIndex : m_TagsList )
			if( nIndex.getTag( ).equals( sValue ) )
				return nIndex;
		
		return null;
	}
	
	/**
	 * Removes a tag from the tags list and clears the tag from any Notes that it tags.
	 * 
	 * @param sTagValue	The String Value of the tag to remove.
	 */
	public void removeTag( String sTagValue )
	{
		Tag removedTag = null;
		
		if( m_TagsList.contains( sTagValue ) )
		{
			removedTag = m_TagsList.get( m_TagsList.indexOf( sTagValue ) );
			
			for( Note nIndex : removedTag.getAdjacentNotes( ) )
				nIndex.removeTag( sTagValue );
			
			m_TagsList.remove( removedTag );
		}
	}
	
	/**
	 * Helper function to return a list of Tags that the Note parameter is connected to.
	 * 
	 * @param nReferenceNote	The Note to cross-reference.
	 * @return					List of Tags that are connected to the note.
	 */
	private ArrayList< Tag > getLinkedTags( Note nReferenceNote )
	{
		ArrayList< Tag > m_ReturnTags = new ArrayList< Tag >( );
		
		for( Tag tIndex : m_TagsList )
			if( tIndex.getAdjacentNotes( ).contains( nReferenceNote ) )
				m_ReturnTags.add( tIndex );
		
		return m_ReturnTags;
	}
	
	/**
	 * Generates and returns a new note that is added to the NotesList.
	 * @return	The new note that's generated with default parameters.
	 */
	public Note loadNewNote( )
	{
		Note nNewNote = new Note( Calendar.getInstance( ), getUniqueTitle( s_DefaultTitle ), "" );
		
		m_NotesList.add( nNewNote );
		
		return nNewNote;
	}
	
	/**
	 * Given a title, parses the notes in the list and appends the count onto the title to produce
	 * a unique title.
	 * 
	 * @param sTitle	The Title to parse.
	 * @return			The unique title.
	 */
	public String getUniqueTitle( String sTitle )
	{
		int iTitleCount = 0;
		String sReturnString = sTitle;
		
		ArrayList< String > m_TitleList = new ArrayList< String >( );
		
		for( Note nIndex : m_NotesList )
			m_TitleList.add( nIndex.getTitle( ) );
		
		while( m_TitleList.contains( sReturnString ) )
			sReturnString = sTitle + " - " + String.valueOf( ++iTitleCount );
		
		return sReturnString;
	}
	
	/**
	 * Returns an array of Notes that are adjacent to tags with the specified substring in their value.
	 * The keyword passed in is compared with each tag to see if the tag starts with the keyword.  If it
	 * does, the Tag's adjacent notes are set into the return list with no duplicates being made.  The 
	 * return list is sorted and returned.
	 * 
	 * @param sKeyWord	The substring to check tags with.
	 * @return			Sorted ArrayList with all connected Notes.
	 */
	public ArrayList< Note > getFilteredNotes( String sKeyWord )
	{
		ArrayList< Note > alReturnList = new ArrayList< Note >( );
		
		for( Tag tIndex : getFilteredTags( sKeyWord ) )
			for( Note nIndex : tIndex.getAdjacentNotes( ) )
				if( !alReturnList.contains( nIndex ) )
					alReturnList.add( nIndex );
		
		return quickSort( alReturnList );
	}
	
	/**
	 * Function that returns a narrowed down list of tags based on a specified keyword.
	 * 
	 * @param sKeyWord	The keyword to filter tags with.
	 * @return			Sorted list of Filtered Tags.
	 */
	public ArrayList< Tag > getFilteredTags( String sKeyWord )
	{
		ArrayList< Tag > alReturnList = new ArrayList< Tag >( );
		
		for( Tag tIndex : m_TagsList )
			if( tIndex.getTag( ).startsWith( sKeyWord ) )
				alReturnList.add( tIndex );
		
		return quickSort( alReturnList );
	}
	
	/**
	 * Public functions for sorting the internal lists.
	 */
	public ArrayList< Note > quickSortNotes( ) { return ( m_NotesList = quickSort( m_NotesList ) ); }
	public ArrayList< Tag > quickSortTags( ) { return ( m_TagsList = quickSort( m_TagsList ) ); }
	
	/**
	 * Function to sort a list of Notes and Tags.
	 * 
	 * @param m_SubList	The list of SortableNoteTag objects to sort.
	 * @return			The sorted list.
	 */
	private < NT extends SortableNoteTag > ArrayList< NT > quickSort( ArrayList< NT > m_SubList )
	{
		
		ArrayList< NT > m_ReturnList	= new ArrayList< NT >( );
		ArrayList< NT > m_Greater 		= new ArrayList< NT >( );
		ArrayList< NT > m_Lesser		= new ArrayList< NT >( );
		NT m_Pivot						= null;
		int iSize 						= m_SubList.size( );
		
		if( iSize <= 1 )
			return m_SubList;
		
		m_Pivot = m_SubList.remove( iSize >> 1 );
		
		for( NT ntIndex : m_SubList )
		{
			if( m_Pivot.compareTo( ntIndex ) > 0 )
				m_Greater.add( ntIndex );
			else
				m_Lesser.add( ntIndex );
		}
		
		m_ReturnList.addAll( quickSort( m_Lesser ) );
		m_ReturnList.add( m_Pivot );
		m_ReturnList.addAll( quickSort( m_Greater ) );
		
		return m_ReturnList;
	}
	
	/**
	 * Removes the specified Note from the list.
	 * @param nNoteToRemove	The Note to remove from the list.
	 */
	public void removeNote( Note nNoteToRemove )
	{
		for( Tag tIndex : getLinkedTags( nNoteToRemove ) )
			if( tIndex.removeNote( nNoteToRemove ) )
				m_TagsList.remove( tIndex );
	
		m_NotesList.remove( nNoteToRemove );
	}
	
	/**
	 * Consolidates the IDs and saves the entire Note List to an XML file.
	 */
	public void saveListToXML( )
	{
		XStream xstream = new XStream( new StaxDriver() );
		PrintStream outFile;

        try
        {
            outFile = new PrintStream( new FileOutputStream( s_FileName ) );  
            outFile.print( xstream.toXML( m_NotesList ) );

            outFile.close();
        }
        catch(Exception ex) {}
	}
}
