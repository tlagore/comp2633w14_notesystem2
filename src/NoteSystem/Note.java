/**
 * 
 */
package NoteSystem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Note Entity containing data pertaining to a Note.
 *
 * @author	James C. Coté
 * @version v1.0 - Feb 14, 2014
 */
public class Note 
{
	// Private Variables
	private int m_iID;
	private String m_sTitle, m_sDesc;
	private ArrayList< Tag > m_Tags;
	private Calendar m_cDate;
	
	// Constants
	private static final int PRIME = 11;
	
	/**
	 * Default Constructor to generate a new Note.
	 */
	public Note( int iNewID, Calendar cNewDate, String sNewTitle, String sNewDesc )
	{
		m_iID 		= iNewID;
		m_cDate 	= cNewDate;
		m_sTitle	= sNewTitle;
		m_sDesc 	= sNewDesc;
		m_Tags		= new ArrayList< Tag >( );
	}
	/**
	 * Separate Copy Constructor for setting a new ID. 
	 */
	public Note( int iID, Note nOther )
    {
		this( nOther );
		this.m_iID = nOther.m_iID;
    }
	
	/**
	 * Default Copy Constructor.
	 */
	public Note( Note nOther )
	{
		this.m_iID 		= nOther.m_iID;
		this.m_sTitle 	= nOther.m_sTitle;
		this.m_sDesc 	= nOther.m_sDesc;
		this.m_cDate	= nOther.m_cDate;
		this.m_Tags		= nOther.m_Tags;
	}
	
	/**
	 * Overriding the inherited hashCode function for comparative purposes.
	 */
	@Override
	public int hashCode( )
	{
		int iReturnCode 	= m_iID * PRIME;
		
		iReturnCode 		+= m_sTitle.hashCode( ) +
							   m_sDesc.hashCode( ) +
							   m_cDate.hashCode( ) +
							   m_Tags.hashCode( );
		
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
			bReturnValue = m_iID == eRHS.m_iID;	
		}
		
		return bReturnValue;
	}
	
	
	
	/**
	 * Getters and Setters
	 */
	public int getID( )								{ return m_iID; }
	public String getTitle( )						{ return m_sTitle; }
	public void setTitle( String sNewTitle )		{ m_sTitle = sNewTitle; }
	public String getDesc( )						{ return m_sDesc; }
	public void setDesc( String sNewDesc )			{ m_sDesc = sNewDesc; }
	public String getDate( )						{ return String.format( "%d-%s-%d", 
																			m_cDate.get( Calendar.DAY_OF_MONTH ), 
																			m_cDate.getDisplayName( Calendar.MONTH, Calendar.SHORT, Locale.CANADA ),
																			m_cDate.get( Calendar.YEAR ) ); }
	public void setDate( long lDateInMilliseconds ) { m_cDate.setTimeInMillis( lDateInMilliseconds ); }
	public ArrayList< Tag > getTags( ) 				{ return m_Tags; }
	
	/**
	 * Functions to add and remove individual tags.
	 */
	public void addTag( Tag tTagToAdd ) 			
	{ 
		if( !m_Tags.contains( tTagToAdd ) )
			m_Tags.add( tTagToAdd );
	}
	public void removeTag( Tag tTagToRemove )	
	{
		m_Tags.remove( tTagToRemove );
	}
}
