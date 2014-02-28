package GUI;


import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import NoteKeeper.*;
import NoteSystem.*;

import java.awt.event.*;

import javax.swing.border.*;

/**
 * The main interface and window that handles interaction of the user with the note system
 * 
 * @version February 25
 * @author Tyrone
 */

//Dont delete me

public class NoteSystemMainWindow extends JFrame {

	private TagListModel tagListModel;
	private NoteListModel noteListModel;
	private DefaultListModel<String> currentTagModelList;
	private Note currentNote;
	private NoteSystem noteSystem;

	private JTextField tagTextField, noteTitleTextField;
	private JButton btnRemove, btnNewNote, btnEdit, btnSave, btnAddTag, btnClose,
					btnRemoveTag, btnClear, btnDiscard;
	private JPanel noteViewPanel, titelBorderPanel,  descriptionBorderPanel,
					searchByTagBorder, tagPanel, contentPane;
	private JTextPane noteDateTextPane, noteDescriptionTextPane;
	private JLabel lblSmartwaterNotes;
	
	private JList<Note> noteJList;
	private JList<String> currentTagJList;	
	private JList<Tag> tagJList;
	
	private JTextField noteTagTextField;
	private JScrollPane noteScrollPane, tagScrollPane, noteTagScrollPane;
	
	private String oldTitle;
	
	//-----------------------------INNER CLASSES----------------------------//
	//		Purpose:  Handles actions that are performed via the GUI		//
	//----------------------------------------------------------------------//
	
	
	/**
	 * Handles all mouse listening for connected objects.
	 * 
	 * @author Tyrone Lagore
	 * @version February 24, 2014
	 */
	public class MainWindowMouseHandlr implements MouseListener {
		private NoteSystemMainWindow parent;
		
		public MainWindowMouseHandlr(NoteSystemMainWindow parent)
		{
			this.parent = parent;
		}

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			/**
			 * When a mouse click is registered while the mouse is in the note J List, a check is done to see if the note 
			 * is changed. If it has been, it means it was not saved before changing notes.  A prompt is displayed
			 * to ask if the user wants to save or not.  After answering, the the selected note is displayed.
			 */
			if (e.getSource().equals(noteJList))
			{				
				if (noteListModel.getElementAt(noteJList.getSelectedIndex()) != currentNote && noteJList.getSelectedValuesList().size() == 1)
				{
					if (checkIfChanged() && getYesNo(parent, "Would you like to save the current note before changing notes?", 
												"Save Note?") == 0)
							saveNote();

					changeNote();
				}else
					editable(false);
				
			}else if (e.getSource().equals(tagJList))
			{
				if (tagListModel.getSize() > 0)
				{
					tagTextField.setText( "" );
					tagChanged( true );
				}
			}
	
		}

		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	
	/**
	 * Handles all key listening for connected objects.
	 * 
	 * @author Tyrone Lagore
	 * @version February 24, 2014
	 */
	public class MainWindowKeyHandlr implements KeyListener {
		private NoteSystemMainWindow parent;
		
		public MainWindowKeyHandlr(NoteSystemMainWindow parent){
			this.parent = parent;
		}

		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e){
			if (e.getSource().equals(tagTextField))
			{
				sortBothByTag();
			}
			else if (e.getSource().equals(noteJList))
				if (noteListModel.getElementAt(noteJList.getSelectedIndex()) != currentNote)
					changeNote();

			else if (e.getSource().equals(tagJList))
					tagChanged( false );
			
		}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	/**
	 * Handles all action listening for connected buttons.
	 * 
	 * @author Tyrone Lagore
	 * @version Ferburary 25, 2014
	 */
	public class MainWindowButtonHandlr implements ActionListener {
		private NoteSystemMainWindow parent;

		public MainWindowButtonHandlr(NoteSystemMainWindow parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) 
		{	
			if (e.getSource().equals(btnEdit)) 
					editable(true);
			
			else if (e.getSource().equals(btnRemove)) 
				removeNote();
			
			else if (e.getSource().equals(btnSave))
				saveNote();
			
			else if (e.getSource().equals(btnNewNote))
				newNote();
			
			else if (e.getSource().equals(btnAddTag))
				addTag();
			
			else if (e.getSource().equals(btnRemoveTag))
				removeTags();
			
			else if (e.getSource().equals(btnClear))
				clearTagField();
			
			else if (e.getSource().equals(btnClose))
				closeSelected();
			
			/*
			 * btnDiscard is only enabled on new notes.  This gives the user the option to cancel their 
			 * request to make a new note.  ONLY available on new notes that have not been saved. If a 
			 * user desires to remove a note that has been added to the system, they must select remove
			 * note.
			 */
			else if (e.getSource().equals(btnDiscard))
			{
				removeNote();
				changeNote();
				editable(false);
				tagJList.clearSelection();
			}
				
		}
		
		/**
		 * Name: removeNote
		 * Purpose: Removes the selected notes from the list.
		 */
		private void removeNote()
		{
			List<Note> selected = noteJList.getSelectedValuesList();
			
			if (getYesNo(parent, "Are you sure you want to remove selected notes?", "Remove selected?") == 0)
			{
				for (Note note : selected)
					noteListModel.removeNote(note);		

				if (noteListModel.getSize() == 0)
					newNote();
				else
				{
					currentNote = noteListModel.getElementAt(0);
					editable(false);	
					updateFields(false);
				}

				noteJList.setSelectedIndex(0);
				tagListModel.fireChange();
			}
		}
		
		/**
		 * Name: newNote
		 * Purpose: Creates a new note that includes the tag that is currently selected.
		 */
		private void newNote()
		{
			List<Tag> selectedTags = tagJList.getSelectedValuesList();
			
			currentNote = noteListModel.loadNewNote(selectedTags);
			
			if( !selectedTags.isEmpty( ) )
				noteListModel.sortByTag( selectedTags.get( 0 ).getTag( ), true );
			
			updateFields(true);
			btnDiscard.setVisible(true);
			editable(true);
		}
		
		/**
		 * Name: addTag
		 * Purpose: Adds the tag currently entered into the tag field on the selected note.
		 */
		private void addTag()
		{
			currentTagModelList.addElement(noteTagTextField.getText().toLowerCase());
			noteTagTextField.setText("");
		}
		
		/**
		 * Name: removeTags
		 * Purpose: Removes any selected tags from the note
		 */
		private void removeTags()
		{
			List<String> selectedTags = currentTagJList.getSelectedValuesList();
			
			for (String t : selectedTags)
				currentTagModelList.removeElement(t);
		}
		
		/**
		 * Name: closeSelected
		 * Purpose: Instantiates and runs the confirm close window
		 */
		private void closeSelected()
		{
			ConfirmCloseWindow cWindow = new ConfirmCloseWindow();
			
			btnClose.setEnabled(false);
		 	cWindow.run(parent);
		}
	}
	//////////////////////////////////////////////////////////////////////


	/**
	 * Name: run
	 * Purpose: Launches the application.
	 */
	public void run() 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run()
			{
				try 
				{
					setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Name: sortBothByTag
	 * Purpose: Sorts both the tag list and note list by tag.  If no tag is selected in the tag list,
	 * 			the list will be sorted by current text in the search by text field.  (Can be empty.)
	 */
	public void sortBothByTag()
	{
		String tag = tagTextField.getText();
		String selectedTag = "";
		
		if( tag != "" )
			tagJList.clearSelection( );
		
		if (tagJList.getSelectedIndex() != -1)
		{
			clearTagField( );
			selectedTag = tagJList.getSelectedValue().getTag();
			
			tagListModel.sortByTag(selectedTag);
			noteListModel.sortByTag(selectedTag, false);
		}
		else
		{
			tagListModel.sortByTag(tag);
			noteListModel.sortByTag(tag, false);
		}
	}
	
	/**
	 * Name: clearTagField
	 * Purpose: Clears any tag information entered and de-selects the current tag.  Sets the current note to the first note
	 * 			in the note list.
	 */
	public void clearTagField()
	{
		tagListModel.clearTagField();
		noteListModel.clearTagField();
		tagTextField.setText("");
		
		tagJList.clearSelection();
		
		noteJList.setSelectedIndex(0);
		changeNote();
	}
	
	/**
	 * Name: tagChanged
	 * Purpose: A new tag has been selected, the note list is sorted by this tag, and the tag becomes the only
	 * 			tag viewable in the tag list (until a reset is issued)
	 * @param bExact true = exact tag matches only, false = begins with tag matches
	 */
	public void tagChanged( boolean bExact )
	{
		noteListModel.sortByTag(tagJList.getSelectedValue().getTag(), bExact);
		tagListModel.setList( tagJList.getSelectedValue( ) );
		noteJList.setSelectedIndex(0);
		tagJList.setSelectedIndex( 0 );
		changeNote();
	}
	
	/**
	 * Name: checkIfChanged
	 * Purpose: Checks to see if any of the fields were altered while the note was in edit mode.
	 * @return boolean true = the note was altered in some way, false = the note was not altered.
	 */
	public boolean checkIfChanged()
	{	
		return (!currentNote.getTitle().equals(noteTitleTextField.getText()) ||
				!currentNote.getDesc().equals(noteDescriptionTextPane.getText()) ||
				!currentNote.getTags().containsAll(Arrays.asList(currentTagModelList.toArray())));
	}

	/**
	 * Name: changeNote
	 * Purpose: A new note has been selected.  The fields are updated to display the contents of the newly selected
	 * 			note and are set to be non-editable.
	 */
	public void changeNote()
	{
		editable(false);

		currentNote = noteListModel.getElementAt(noteJList.getSelectedIndex());
		updateFields(false);
	}
	
	/**
	 * Name: editable
	 * Purpose: Toggles whether certain fields are editable or not based on the boolean
	 * 			passed in.
	 *  
	 * @param enable true = set editable to true, false = set editable to false
	 * 
	 */
	private void editable(boolean enable)
	{
		if (noteJList.getSelectedValuesList().size() > 1)
			enable = false;
		
		Color color = enable ? new Color(240,248,255) : SystemColor.controlHighlight;
		
		btnEdit.setEnabled					(!enable);
		btnSave.setEnabled					(enable);
		btnAddTag.setEnabled				(enable);
		btnRemoveTag.setEnabled				(enable);
		noteTitleTextField.setEditable		(enable);
		noteDescriptionTextPane.setEditable	(enable);
		noteTagTextField.setEditable		(enable);
		
		noteTagTextField.setBackground(color);
		noteDescriptionTextPane.setBackground(color);
		noteTitleTextField.setBackground(color);
		currentTagJList.setBackground(color);
		
		if (enable)
		{
			noteTitleTextField.grabFocus();
			noteTitleTextField.selectAll();
		}else
			btnDiscard.setVisible(false);
		
	}
	
	/**
	 * Name: updateFields
	 * Purpose: updateFields updates the visual presentation of the Title, Date, Description
	 * 			and Tags of the current note being viewed.
	 */
	private void updateFields(boolean isNewNote)
	{
		noteTitleTextField.setText(currentNote.getTitle());
		oldTitle = currentNote.getTitle();
		noteDateTextPane.setText(currentNote.getDate());
		noteDescriptionTextPane.setText(currentNote.getDesc());
		
		if (isNewNote)
			noteDescriptionTextPane.setText("<Enter description>");
		
		loadCurrentTags();
	}
	
	/**
	 * Name: saveNote
	 * Purpose: Saves the fields that are currently in the Main Window to the internal
	 * 			note.  If the title has changed, the title is also saved. 
	 */
	public void saveNote() 
	{
		noteTagTextField.setText("");
		
		currentNote.setDesc(noteDescriptionTextPane.getText());
		currentNote.updateDate();	
		
		// Sets the currentNode's tag list to mimic the currentTagModelList.
		ArrayList<String> noteTags = new ArrayList<String>();
		String sCurr = "";
		
		noteTags.addAll(currentNote.getTags());
		
		for (int i = 0; i < currentTagModelList.getSize(); i++)
		{
			sCurr = currentTagModelList.getElementAt( i );
			
			if (!noteTags.contains( sCurr ))
				currentNote.addTag( sCurr );
			
			noteTags.remove( sCurr );
		}
		
		for( String sIndex : noteTags )
			currentNote.removeTag( sIndex );
	
		// Save Note
		noteListModel.updateNote(currentNote, noteTitleTextField.getText(), oldTitle);
		if( tagJList.getSelectedIndex( ) != -1 )
			noteListModel.sortByTag( tagJList.getSelectedValue( ).getTag( ), false );
		else
			tagListModel.fireChange();
		
		
		
		// Update List viewing
		changeNote();
	}
	
	/**
	 * Name: loadCurrentTags
	 * Purpose: Loads the tags of the selected note into the list of tags within the 
	 * 			note view.
	 */
	private void loadCurrentTags()
	{
		ArrayList<String> currentTags = currentNote.getTags();
		
		currentTagModelList.removeAllElements();
		for (String t : currentTags)
			currentTagModelList.addElement(t);
	}

	/**
	 * Name: confirmCloseWIndowHasClosed
	 * Purpose: Lets the main window know that the confirm window has closed by method
	 * 			of decline or irregular close (closing via the top right x)
	 */
	public void confirmCloseWindowHasClosed() 
	{
		btnClose.setEnabled(true);
	}

	/**
	 * Name exitAndSave
	 * Purpose: Saves the current list to an XML file, closes the window, and terminates the
	 * 			program by disposing of all connected resources.
	 */
	public void exitAndSave() 
	{
		noteSystem.saveListToXML();
		setVisible(false);
		dispose();
	}
	
	/**
	 * Name: getYesNo
	 * Purpose: Creates a simple yes or no frame for confirmation based events.
	 * @param parent The calling object
	 * @param prompt The prompt that will be displayed in the window
	 * @param title The title that will be displayed in the header of the window
	 * @return int 0 returned means yes was selected, 1 returned means no was selected
	 */
	private int getYesNo(Component parent, String prompt, String title)
	{
		Object[] options = {"Yes", "No"};
		return JOptionPane.showOptionDialog(parent, prompt, title, 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
				options, options[0]);
	}
	
	/**
	 * Name: NoteSystemMainWindow
	 * Purpose: Constructor for the Main window, creates the actual frame and all connected
	 * 			buttons/panels/frames.
	 * @param noteSystem Reference to the internal note system used 
	 * @param note The first note that the system will load to view (always the first)
	 */
	public NoteSystemMainWindow(NoteSystem noteSystem, Note note)
	{
		this.noteSystem = noteSystem;

		MainWindowButtonHandlr handler = new MainWindowButtonHandlr(this);
		MainWindowKeyHandlr keyHandler = new MainWindowKeyHandlr(this);
		MainWindowMouseHandlr mouseHandler = new MainWindowMouseHandlr(this);
		currentNote = note;
		
		oldTitle = currentNote.getTitle();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				exitAndSave();
			}
		});
		
		setBounds( 100, 100, 833, 532 );
		contentPane = new JPanel( );
		contentPane.setLocation(-36, -67);
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout(null);
		
		btnNewNote = new JButton("New Note");
		btnNewNote.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnNewNote.setToolTipText("Create a new, empty note.");
		btnNewNote.addActionListener(handler);
		btnNewNote.setBounds(481, 20, 173, 23);
		contentPane.add(btnNewNote);
		
		btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnRemove.setToolTipText("Select one or more notes and this will remove the notes from the system.");
		btnRemove.addActionListener(handler);
		btnRemove.setBounds(481, 450, 173, 23);
		contentPane.add(btnRemove);
		
		searchByTagBorder = new JPanel();
		searchByTagBorder.setBorder(new TitledBorder(null, "Search by Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchByTagBorder.setBounds(693, 0, 105, 51);
		contentPane.add(searchByTagBorder);
		searchByTagBorder.setLayout(null);
		
		tagTextField = new JTextField();
		tagTextField.setFont(new Font("Dotum", Font.PLAIN, 11));
		tagTextField.addKeyListener(keyHandler);
		tagTextField.setBounds(10, 16, 89, 24);
		tagTextField.setBackground(SystemColor.controlHighlight);
		searchByTagBorder.add(tagTextField);
		tagTextField.setColumns(1);

		btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnEdit.setToolTipText("Allows you to edit the contents of the selected note.");
		btnEdit.addActionListener(handler);
		btnEdit.setBounds(10, 20, 89, 23);
		contentPane.add(btnEdit);
		
		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnSave.setToolTipText("Saves changes made to the current note.  Don't worry, if you forget to click save, the note will be saved automatically.");
		btnSave.addActionListener(handler);
		btnSave.setEnabled(false);
		btnSave.setBounds(10, 450, 89, 23);
		contentPane.add(btnSave);
		
		btnClose = new JButton("Save and Exit");
		btnClose.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnClose.setToolTipText("U sur u wan do dat bro?");
		btnClose.addActionListener(handler);
		btnClose.setBounds(693, 401, 111, 82);
		contentPane.add(btnClose);
		
		lblSmartwaterNotes = new JLabel("SmartWater Notes");
		lblSmartwaterNotes.setForeground(new Color(0, 0, 0));
		lblSmartwaterNotes.setFont(new Font("Lucida Handwriting", Font.BOLD, 18));
		lblSmartwaterNotes.setBounds(169, 20, 261, 18);
		contentPane.add(lblSmartwaterNotes);
		
		noteViewPanel = new JPanel();
		noteViewPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		noteViewPanel.setBounds(20, 54, 433, 386);
		contentPane.add(noteViewPanel);
		noteViewPanel.setLayout(null);
		
		titelBorderPanel = new JPanel();
		titelBorderPanel.setBorder(new TitledBorder(null, "Title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		titelBorderPanel.setBounds(24, 14, 331, 51);
		noteViewPanel.add(titelBorderPanel);
		titelBorderPanel.setLayout(null);
		
		noteTitleTextField = new JTextField();
		noteTitleTextField.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteTitleTextField.setToolTipText("Enter a title for your note.");
		noteTitleTextField.setEditable(false);
		noteTitleTextField.setBounds(10, 16, 311, 24);
		titelBorderPanel.add(noteTitleTextField);
		noteTitleTextField.setBackground(SystemColor.controlHighlight);
		
		JPanel dateBorderPanel = new JPanel();
		dateBorderPanel.setBorder(new TitledBorder(null, "Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dateBorderPanel.setBounds(24, 76, 331, 51);
		noteViewPanel.add(dateBorderPanel);
		dateBorderPanel.setLayout(null);
		
		noteDateTextPane = new JTextPane();
		noteDateTextPane.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteDateTextPane.setEditable(false);
		noteDateTextPane.setBackground(SystemColor.menu);
		noteDateTextPane.setBounds(10, 16, 311, 24);
		dateBorderPanel.add(noteDateTextPane);
		
		descriptionBorderPanel = new JPanel();
		descriptionBorderPanel.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		descriptionBorderPanel.setBounds(24, 187, 399, 194);
		noteViewPanel.add(descriptionBorderPanel);
		descriptionBorderPanel.setLayout(null);
		
		noteDescriptionTextPane = new JTextPane();
		noteDescriptionTextPane.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteDescriptionTextPane.setToolTipText("Enter a description for your note by selecting a current note and clicking \"Edit\" or by creating a \"New Note\"");
		noteDescriptionTextPane.setEditable(false);
		noteDescriptionTextPane.setBounds(10, 16, 273, 169);
		descriptionBorderPanel.add(noteDescriptionTextPane);
		noteDescriptionTextPane.setBackground(SystemColor.controlHighlight);
		
		noteTagScrollPane = new JScrollPane();
		noteTagScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		noteTagScrollPane.setBounds(293, 16, 96, 169);
		descriptionBorderPanel.add(noteTagScrollPane);
		currentTagModelList = new DefaultListModel<String>();
		currentTagJList = new JList();
		currentTagJList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		currentTagJList.setBackground(SystemColor.controlHighlight);
		currentTagJList.setModel(currentTagModelList);
		noteTagScrollPane.setViewportView(currentTagJList);
		
		tagPanel = new JPanel();
		tagPanel.setBorder(new TitledBorder(null, "Add Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tagPanel.setBounds(24, 126, 130, 51);
		noteViewPanel.add(tagPanel);
		tagPanel.setLayout(null);
		
		noteTagTextField = new JTextField();
		noteTagTextField.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteTagTextField.setToolTipText("Enter a tag for your note and select \"Add Tag\".  When you save your note, the new tag will appear in the tag window to the right.");
		noteTagTextField.setBounds(10, 16, 115, 24);
		tagPanel.add(noteTagTextField);
		noteTagTextField.setBackground(SystemColor.controlHighlight);
		noteTagTextField.setColumns(1);
		noteTagTextField.setEditable(false);
		
		btnAddTag = new JButton("Add Tag");
		btnAddTag.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnAddTag.addActionListener(handler);
		btnAddTag.setBounds(164, 145, 93, 23);
		noteViewPanel.add(btnAddTag);
		btnAddTag.setEnabled(false);
		
		btnRemoveTag = new JButton("Remove Tag");
		btnRemoveTag.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnRemoveTag.addActionListener(handler);
		btnRemoveTag.setBounds(307, 145, 116, 23);
		noteViewPanel.add(btnRemoveTag);
		btnRemoveTag.setEnabled(false);
		
		noteScrollPane = new JScrollPane();
		noteScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		noteScrollPane.setBounds(461, 53, 230, 386);
		contentPane.add(noteScrollPane);
		
		
		noteListModel = new NoteListModel(noteSystem);
		noteJList = new JList();
		noteJList.addKeyListener(keyHandler);
		noteJList.setFont(new Font("SimHei", Font.PLAIN, 14));
		noteJList.setToolTipText("This view contains your current notes.  Select a note to view it's contents.  Press \"Edit\" to modify the contents.");
		noteJList.addMouseListener(mouseHandler);
		noteJList.setModel(noteListModel);
		noteScrollPane.setViewportView(noteJList);
		noteJList.setBorder(null);
		noteJList.setBackground(SystemColor.control);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tagScrollPane.setBounds(699, 86, 99, 304);
		contentPane.add(tagScrollPane);
		
		tagListModel = new TagListModel(noteSystem);
		tagJList = new JList();
		tagJList.addKeyListener(keyHandler);
		tagJList.setFont(new Font("SimHei", Font.PLAIN, 13));
		tagJList.addMouseListener(mouseHandler);
		tagJList.setModel(tagListModel);
		tagScrollPane.setViewportView(tagJList);
		tagJList.setBorder(null);
		tagJList.setBackground(SystemColor.control);
		
		btnClear = new JButton("Reset");
		btnClear.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnClear.setToolTipText("Go back to your full note and tag list.");
		btnClear.addActionListener(handler);
		btnClear.setBounds(703, 53, 89, 23);
		contentPane.add(btnClear);
		
		btnDiscard = new JButton("Discard");
		btnDiscard.setToolTipText("Only available for newly created notes that have not been saved.");
		btnDiscard.addActionListener(handler);
		btnDiscard.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnDiscard.setEnabled(true);
		btnDiscard.setBounds(364, 450, 89, 23);
		contentPane.add(btnDiscard);
		btnDiscard.setVisible(false);
		
		updateFields(false);
		loadCurrentTags();
		
		noteJList.setSelectedIndex(0);
	}
}
