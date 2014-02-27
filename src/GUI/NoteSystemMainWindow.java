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

public class NoteSystemMainWindow extends JFrame {

	private TagListModel tagListModel;
	private NoteListModel noteListModel;
	private DefaultListModel<String> currentTagModelList;
	private Note currentNote;
	private NoteSystem noteSystem;

	private JTextField tagTextField;
	private JButton btnRemove, btnNewNote, btnEdit, btnSave, btnAddTag, btnClose,
					btnRemoveTag, btnClear;
	private JPanel noteViewPanel, titelBorderPanel,  descriptionBorderPanel,
					searchByTagBorder, tagPanel, contentPane;
	private JTextPane noteDateTextPane, noteTitleTextPane, noteDescriptionTextPane;
	private JLabel lblSmartwaterNotes;
	
	private JList<Note> noteJList;
	private JList<String> currentTagJList;	
	private JList<Tag> tagJList;
	
	private JTextField noteTagTextField;
	private JScrollPane noteScrollPane, tagScrollPane, noteTagScrollPane;
	
	private String oldTitle;
	private boolean noteChanged;
	


	
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
			if (e.getSource().equals(noteJList))
			{				
				if (noteChanged)
				{
					/*ConfirmChangeNote changeWindow = new ConfirmChangeNote();
					changeWindow.run(parent);*/
					
					Object[] options = {"Yes", "No"};
					int n = JOptionPane.showOptionDialog(parent, "Would you like to save the note before changing notes?", "Save Note?", 
																	JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
																	options, options[0]);
					
					if (n == 0)
						saveNote();
				}
				
				editable(false);

				currentNote = noteListModel.getElementAt(noteJList.getSelectedIndex());
				updateFields();
			}else if (e.getSource().equals(tagJList))
			{
				noteListModel.sortByTag(tagJList.getSelectedValue().getTag());
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
				tagListModel.sortByTag(tagTextField.getText());
				noteListModel.sortByTag(tagTextField.getText());
			}
		}
		@Override
		public void keyTyped(KeyEvent e) 
		{
			noteChanged = true;
		}
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
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource().equals(btnEdit)) 
					editable(true);
			
			else if (e.getSource().equals(btnRemove)) 
			{
				List<Note> selected = noteJList.getSelectedValuesList();
				
				for (Note note : selected)
					noteListModel.removeNote(note);		
				
				if (noteListModel.getSize() == 0)
				{
					currentNote = noteListModel.loadNewNote();
					noteChanged = false;
					editable(true);
				}
				else
					currentNote = noteListModel.getElementAt(0);
				
				updateFields();
				tagListModel.fireChange();
				
			} else if (e.getSource().equals(btnSave))
			{
				saveNote();
				editable(false);
				
			} else if (e.getSource().equals(btnNewNote))
			{
				currentNote = noteListModel.loadNewNote();
				updateFields();
				editable(true);
				
			} else if (e.getSource().equals(btnAddTag))
			{
				currentNote.addTag(noteTagTextField.getText());
				noteChanged = true;
				noteTagTextField.setText("");
				loadCurrentTags();
				
			} else if (e.getSource().equals(btnRemoveTag))
			{
				List<Tag> selectedTags = tagJList.getSelectedValuesList();
				
				for (Tag t : selectedTags)
				{
					currentNote.removeTag(t.getTag());
				}
				
				noteChanged = true;
				loadCurrentTags();
			} else if (e.getSource().equals(btnClear))
			{
				tagListModel.clearTagField();
				noteListModel.clearTagField();
				tagTextField.setText("");
			} else if (e.getSource().equals(btnClose))
			{
				ConfirmCloseWindow cWindow = new ConfirmCloseWindow();
				
				btnClose.setEnabled(false);
			 	cWindow.run(parent);
			}
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
	 * Name: editable
	 * Purpose: Toggles whether certain fields are editable or not based on the boolean
	 * 			passed in.
	 *  
	 * @param enable true = set editable to true, false = set editable to false
	 * 
	 */
	
	private void editable(boolean enable)
	{
		Color color = enable ? new Color(240,248,255) : SystemColor.controlHighlight;
		
		btnEdit.setEnabled					(!enable);
		btnSave.setEnabled					(enable);
		btnAddTag.setEnabled				(enable);
		btnRemoveTag.setEnabled				(enable);
		noteTitleTextPane.setEditable		(enable);
		noteDescriptionTextPane.setEditable	(enable);
		noteTagTextField.setEditable		(enable);
		
		noteTagTextField.setBackground(color);
		noteDescriptionTextPane.setBackground(color);
		noteTitleTextPane.setBackground(color);
		currentTagJList.setBackground(color);
		
	}
	
	/**
	 * Name: updateFields
	 * Purpose: updateFields updates the visual presentation of the Title, Date, Description
	 * 			and Tags of the current note being viewed.
	 */
	private void updateFields()
	{
		noteTitleTextPane.setText(currentNote.getTitle());
		
		oldTitle = currentNote.getTitle();
		
		noteDateTextPane.setText(currentNote.getDate());
		noteDescriptionTextPane.setText(currentNote.getDesc());
		
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
	
		noteListModel.updateNote(currentNote, noteTitleTextPane.getText(), oldTitle);
		
		updateFields();
		tagListModel.fireChange();
		
		noteChanged = false;
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
		
		noteChanged = false;
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
		btnClose.setToolTipText("U sur u wan do that bro?");
		btnClose.addActionListener(handler);
		btnClose.setBounds(693, 401, 111, 82);
		contentPane.add(btnClose);
		
		lblSmartwaterNotes = new JLabel("SmartWater Notes");
		lblSmartwaterNotes.setFont(new Font("Lucida Handwriting", Font.BOLD, 16));
		lblSmartwaterNotes.setBounds(290, 11, 195, 18);
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
		
		noteTitleTextPane = new JTextPane();
		noteTitleTextPane.addKeyListener(keyHandler);
		noteTitleTextPane.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteTitleTextPane.setToolTipText("Enter a title for your note.");
		noteTitleTextPane.setEditable(false);
		noteTitleTextPane.setBounds(10, 16, 311, 24);
		titelBorderPanel.add(noteTitleTextPane);
		noteTitleTextPane.setBackground(SystemColor.controlHighlight);
		
		JPanel dateBorderPanel = new JPanel();
		dateBorderPanel.setBorder(new TitledBorder(null, "Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dateBorderPanel.setBounds(24, 76, 331, 51);
		noteViewPanel.add(dateBorderPanel);
		dateBorderPanel.setLayout(null);
		
		noteDateTextPane = new JTextPane();
		noteDateTextPane.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteDateTextPane.setEditable(false);
		noteDateTextPane.setBackground(SystemColor.controlHighlight);
		noteDateTextPane.setBounds(10, 16, 311, 24);
		dateBorderPanel.add(noteDateTextPane);
		
		descriptionBorderPanel = new JPanel();
		descriptionBorderPanel.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		descriptionBorderPanel.setBounds(24, 187, 399, 194);
		noteViewPanel.add(descriptionBorderPanel);
		descriptionBorderPanel.setLayout(null);
		
		noteDescriptionTextPane = new JTextPane();
		noteDescriptionTextPane.addKeyListener(keyHandler);
		noteDescriptionTextPane.setFont(new Font("Dotum", Font.PLAIN, 12));
		noteDescriptionTextPane.setToolTipText("Enter a description for your note by selecting a current note and clicking \"Edit\" or by creating a \"New Note\"");
		noteDescriptionTextPane.setEditable(false);
		noteDescriptionTextPane.setBounds(10, 16, 273, 169);
		descriptionBorderPanel.add(noteDescriptionTextPane);
		noteDescriptionTextPane.setBackground(SystemColor.controlHighlight);
		
		noteTagScrollPane = new JScrollPane();
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
		btnRemoveTag.setBounds(311, 145, 101, 23);
		noteViewPanel.add(btnRemoveTag);
		btnRemoveTag.setEnabled(false);
		
		noteScrollPane = new JScrollPane();
		noteScrollPane.setBounds(453, 53, 230, 386);
		contentPane.add(noteScrollPane);
		
		
		noteListModel = new NoteListModel(noteSystem);
		noteJList = new JList();
		noteJList.setFont(new Font("SimHei", Font.PLAIN, 14));
		noteJList.setToolTipText("This view contains your current notes.  Select a note to view it's contents.  Press \"Edit\" to modify the contents.");
		noteJList.addMouseListener(mouseHandler);
		noteJList.setModel(noteListModel);
		noteScrollPane.setViewportView(noteJList);
		noteJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		noteJList.setBackground(SystemColor.control);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(699, 86, 99, 304);
		contentPane.add(tagScrollPane);
		
		tagListModel = new TagListModel(noteSystem);
		tagJList = new JList();
		tagJList.setFont(new Font("SimHei", Font.PLAIN, 13));
		tagJList.addMouseListener(mouseHandler);
		tagJList.setModel(tagListModel);
		tagScrollPane.setViewportView(tagJList);
		tagJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		tagJList.setBackground(SystemColor.control);
		
		btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Dotum", Font.PLAIN, 11));
		btnClear.setToolTipText("Go back to your full note and tag list.");
		btnClear.addActionListener(handler);
		btnClear.setBounds(703, 53, 89, 23);
		contentPane.add(btnClear);
		
		updateFields();
		loadCurrentTags();
	}
}
