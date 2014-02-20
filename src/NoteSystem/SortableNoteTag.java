/**
 * 
 */
package NoteSystem;

/**
 * Superclass of the Note and Tag Entity classes.  Allows for Note and Tag
 * to be sortable by implementing the abstract compareTo function.
 *
 * @author	James C. Coté
 * @version v1.0 - Feb 18, 2014
 */
public abstract class SortableNoteTag 
{
	public abstract int compareTo( SortableNoteTag eRHS );
	public abstract String toString( );
}
