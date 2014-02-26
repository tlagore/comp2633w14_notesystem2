/**
 * 
 */
package NoteSystem;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/**
 * Note Entity containing data pertaining to a Note.
 *
 * @author	James C. Coté
 * @version v1.1 - Feb 18, 2014
 */
public class Note extends SortableNoteTag
{
	// Private Variables
	private String m_sTitle, m_sDesc;
	private ArrayList< String > m_sTags;
	private Calendar m_cDate;
	
	// Constants
	private static final int PRIME = 11;
	private static final int COL_WIDTH = 15;
	
	/**
	 * Default Constructor to generate a new Note.
	 */
	public Note( Calendar cNewDate, String sNewTitle, String sNewDesc )
	{
		m_cDate 	= cNewDate;
		m_sTitle	= sNewTitle;
		m_sDesc 	= sNewDesc;
		m_sTags		= new ArrayList< String >( );
	}
	
	/**
	 * Default Copy Constructor.
	 */
	public Note( Note nOther )
	{
		this.m_sTitle 	= nOther.m_sTitle;
		this.m_sDesc 	= nOther.m_sDesc;
		this.m_cDate	= nOther.m_cDate;
		this.m_sTags	= nOther.m_sTags;
	}
	
	/**
	 * Overriding the inherited hashCode function for comparative purposes.
	 */
	@Override
	public int hashCode( )
	{
		int iReturnCode 	= 0;
		
		iReturnCode 		+= m_sTitle.hashCode( ) +
							   m_sDesc.hashCode( ) +
							   m_cDate.hashCode( ) +
							   m_sTags.hashCode( );
		
		return iReturnCode;
	}
	
	/**
	 * Overloading Equals to properly compare 2 Note Objects.
	 */
	@Override
	public boolean equals( Object obj )
	{
		// Local Variables
		boolean bReturnValue	= false;
		Note eRHS				= null;
		
		if( null == obj )
			bReturnValue = false;
		else if( obj == this )
			bReturnValue = true;
		else if( !( obj.getClass( ) == this.getClass( ) ) )
			bReturnValue = false;
		else
		{
			eRHS = ( Note )obj;
			bReturnValue = 	m_sTitle == eRHS.m_sTitle &&
							m_cDate.equals( eRHS.m_cDate ) &&
							m_sTags.equals( eRHS.m_sTags ) &&
							m_sDesc.equals( eRHS.m_sDesc );
		}
		
		return bReturnValue;
	}
	
	/**
	 * Getters and Setters
	 */
	public String getTitle( )						{ return m_sTitle; }
	public void setTitle( String sNewTitle )		{ m_sTitle = sNewTitle; }
	public String getDesc( )						{ return m_sDesc; }
	public void setDesc( String sNewDesc )			{ m_sDesc = sNewDesc; }
	public String getDate( )						{ return String.format( "%d-%s-%d", 
																			m_cDate.get( Calendar.DAY_OF_MONTH ), 
																			m_cDate.getDisplayName( Calendar.MONTH, Calendar.SHORT, Locale.CANADA ),
																			m_cDate.get( Calendar.YEAR ) ); }
	public void updateDate( ) 						{ m_cDate.setTime( new Date( ) ); }
	public ArrayList< String > getTags( ) 			{ return m_sTags; }
	
	/**
	 * Functions to add and remove individual tags.
	 */
	public void addTag( String rTagToAdd ) 			
	{ 
		if( !m_sTags.contains( rTagToAdd ) )
			m_sTags.add( rTagToAdd );
	}
	public void removeTag( String rTagToRemove )	
	{
		m_sTags.remove( rTagToRemove );
	}
	
	/**
	 * Comparative function for sorting an array list of notes.
	 * overriding abstract method in SortableNoteTag class.
	 * 
	 * @param nLHS	The first object to compare.
	 * @param nRHS	Second object to compare.
	 * @return		0 if both notes have the same date, a negative number if the left
	 * 				note has an earlier date than the right and positive if the left note has
	 * 				a later date than the right.
	 */
	@Override
	public int compareTo( SortableNoteTag ntRHS )
    {
		Note nRHS = (Note) ntRHS;
	    return this.m_cDate.compareTo( nRHS.m_cDate );
    }
	
	/**
	 * Returns the the string output of a Note class.
	 */
	@Override
	public String toString( )
	{
		String sFormatString = "%-" + COL_WIDTH + "s%" + COL_WIDTH + "s";
		String sAmendedTitle = m_sTitle.length( ) < COL_WIDTH ? m_sTitle : m_sTitle.substring( 0, COL_WIDTH - 4 ) + "..." ;
		
		while( sAmendedTitle.length( ) < COL_WIDTH )
			sAmendedTitle += " ";
				
		return String.format( sFormatString, sAmendedTitle, getDate( ) );
	}
}
