/**
 * 
 */
package NoteSystem;

import java.io.*;
import java.util.*;
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
	private int m_iNextID;
	
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
		m_iNextID = m_NotesList.size( ) + 1;
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
        try
        {
            XStream xstream = new XStream(new StaxDriver() );
            Scanner input = new Scanner( new File( s_FileName ) );
            
            m_NotesList = ( ArrayList< Note > ) xstream.fromXML( input.nextLine() );
            
            populateTagsList( );   

        }
        catch(Exception ex) {} 
        
        if( m_NotesList.isEmpty( ) )
        	loadNewNote( );
	}
	
	/**
	 * Populates the Tag list based on the current Node List.
	 */
	private void populateTagsList( )
	{
		for( Note nIndex : m_NotesList )
			for( Tag tIndex : nIndex.getTags( ) )
			{
				if( m_TagsList.contains( tIndex ) )
					tIndex.addNote( nIndex );
				else
					m_TagsList.add( tIndex );
			}
	}
		
	/**
	 * Re-evaluates list with fresh IDs in order to avoid ID collisions.
	 */
	private void consolidateIDs( )
	{
		m_iNextID = 0;
		
		for( Note nIndex : m_NotesList )
		{
			if( nIndex.getID( ) != m_iNextID )
			{
				m_NotesList.add( m_iNextID, new Note( m_iNextID, nIndex ) );
				m_NotesList.remove( nIndex );
			}
			
			m_iNextID++;
		}
	}
	
	/**
	 * Generates and returns a new note that is added to the NotesList.
	 * @return	The new note that's generated with default parameters.
	 */
	public Note loadNewNote( )
	{
		Note nNewNote = new Note( m_iNextID++, Calendar.getInstance( ), getUniqueTitle( s_DefaultTitle ), "" );
		
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
		
		for( Note nIndex : m_NotesList )
			if( nIndex.getTitle( ).equals( sTitle ) )
				iTitleCount++;
		
		if( iTitleCount != 0 )
			sReturnString += String.valueOf( iTitleCount );
		
		return sReturnString;
	}
	
	/**
	 * Function to sort a list of Notes and Tags.
	 * 
	 * @param m_SubList	The list of SortableNoteTag objects to sort.
	 * @return			The sorted list.
	 */
	public < NT extends SortableNoteTag > ArrayList< NT > quickSort( ArrayList< NT > m_SubList )
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
	 * Removes the first Note with the specified Title
	 * @param sTitles	An array of multiple titles passed into the function.
	 * 					Allows for the removal of multiple Notes at once.
	 */
	public void removeNotes( String... sTitles )
	{
		List< String > slTitles = Arrays.asList( sTitles );
		
		for( Note nIndex : m_NotesList )
		{
			if( slTitles.contains( nIndex.getTitle( ) ) )
			{
				for( Tag tIndex : nIndex.getTags( ) )
					if( tIndex.removeNote( nIndex ) )
						m_TagsList.remove( tIndex );
				
				m_NotesList.remove( nIndex );
			}
		}
	}
	
	/**
	 * Consolidates the IDs and saves the entire Note List to an XML file.
	 */
	public void saveListToXML( )
	{
		XStream xstream = new XStream( new StaxDriver() );
		PrintStream outFile;
		
		consolidateIDs( );

        try
        {
            outFile = new PrintStream( new FileOutputStream( s_FileName ) );  
            outFile.print( xstream.toXML( m_NotesList ) );

            outFile.close();
        }
        catch(Exception ex) {}
	}
}
