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

public class NoteSystemMainWindow extends JFrame {

	private JPanel	contentPane;
	private TagListModel tagModelList;
	private NoteListModel noteModelList;
	
//	private DefaultListModel<Note> noteModelList;
//	private DefaultListModel<Tag> tagModelList;
	private DefaultListModel<String> currentTagModelList;
	private Note currentNote;
	private JTextField tagTextField;
	private JButton btnRemove, btnNewNote, btnEdit, btnSave, btnAddTag, btnClose;
	private JPanel noteViewPanel;
	private JPanel titelBorderPanel;
	private JTextPane noteDateTextPane;
	private JTextPane noteTitleTextPane;
	private JPanel descriptionBorderPanel;
	private JTextPane noteDescriptionTextPane;
	private JPanel searchByTagBorder;
	private JLabel lblSmartwaterNotes;
	private JList noteJList, tagJList, currentTagJList;
	private JPanel tagPanel;
	private JTextField noteTagTextField;
	private JScrollPane noteScrollPane;
	private JScrollPane tagScrollPane;
	private JScrollPane noteTagScrollPane;
	private JButton btnRemoveTag;
	
	private NoteSystem noteSystem;

	
	//-------------------------------INNER CLASS ---------------------------//
	//Purpose:  Handles actions that are performed via the GUI				//
	//----------------------------------------------------------------------//
	public class MainWindowButtonHandlr implements ActionListener {
		private NoteSystemMainWindow parent;

		public MainWindowButtonHandlr(NoteSystemMainWindow parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource().equals(btnEdit)) 
					toggleEdit();
			
			else if (e.getSource().equals(btnRemove)) 
			{
				List<Note> selected = noteJList.getSelectedValuesList();
				
				for (Note note : selected)
					noteModelList.removeNote(note);		
				
			} else if (e.getSource().equals(btnSave))
			{
				
				saveNote();
				toggleEdit();
				
			} else if (e.getSource().equals(btnNewNote))
			{
				currentNote = noteModelList.loadNewNote();
				
				
			} else if (e.getSource().equals(btnAddTag))
			{
				currentNote.addTag(noteTagTextField.getText());
				loadCurrentTags();
				
			} else if (e.getSource().equals(btnRemoveTag))
			{
				List<String> selectedTags = currentTagJList.getSelectedValuesList();
				
				for (String t : selectedTags)
					currentNote.removeTag(t);
				
				loadCurrentTags();
			}
		}
	}
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Create the frame.
	 */
	public NoteSystemMainWindow(NoteSystem noteSystem, Note note)
	{
		this.noteSystem = noteSystem;
		MainWindowButtonHandlr handler = new MainWindowButtonHandlr(this);
		currentNote = note;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 833, 532 );
		contentPane = new JPanel( );
		contentPane.setLocation(-36, -67);
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout(null);
		
		btnNewNote = new JButton("New Note");
		btnNewNote.addActionListener(handler);
		btnNewNote.setBounds(510, 19, 173, 23);
		contentPane.add(btnNewNote);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(handler);
		btnRemove.setBounds(510, 450, 173, 23);
		contentPane.add(btnRemove);
		
		searchByTagBorder = new JPanel();
		searchByTagBorder.setBorder(new TitledBorder(null, "Search by Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchByTagBorder.setBounds(699, 0, 105, 51);
		contentPane.add(searchByTagBorder);
		searchByTagBorder.setLayout(null);
		
		tagTextField = new JTextField();
		tagTextField.setBounds(10, 16, 89, 24);
		tagTextField.setBackground(SystemColor.controlHighlight);
		searchByTagBorder.add(tagTextField);
		tagTextField.setColumns(1);

		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(handler);
		btnEdit.setBounds(10, 20, 89, 23);
		contentPane.add(btnEdit);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(handler);
		btnSave.setEnabled(false);
		btnSave.setBounds(10, 450, 89, 23);
		contentPane.add(btnSave);
		
		btnClose = new JButton("Save and Exit");
		btnClose.addActionListener(handler);
		btnClose.setBounds(699, 401, 105, 72);
		contentPane.add(btnClose);
		
		lblSmartwaterNotes = new JLabel("SmartWater Notes");
		lblSmartwaterNotes.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSmartwaterNotes.setBounds(306, 11, 147, 18);
		contentPane.add(lblSmartwaterNotes);
		
		noteViewPanel = new JPanel();
		noteViewPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		noteViewPanel.setBounds(10, 53, 478, 386);
		contentPane.add(noteViewPanel);
		noteViewPanel.setLayout(null);
		
		titelBorderPanel = new JPanel();
		titelBorderPanel.setBorder(new TitledBorder(null, "Note Title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		titelBorderPanel.setBounds(24, 14, 429, 51);
		noteViewPanel.add(titelBorderPanel);
		titelBorderPanel.setLayout(null);
		
		noteTitleTextPane = new JTextPane();
		noteTitleTextPane.setEditable(false);
		noteTitleTextPane.setBounds(10, 16, 409, 24);
		titelBorderPanel.add(noteTitleTextPane);
		noteTitleTextPane.setBackground(SystemColor.controlHighlight);
		
		JPanel dateBorderPanel = new JPanel();
		dateBorderPanel.setBorder(new TitledBorder(null, "Note Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dateBorderPanel.setBounds(24, 76, 429, 51);
		noteViewPanel.add(dateBorderPanel);
		dateBorderPanel.setLayout(null);
		
		noteDateTextPane = new JTextPane();
		noteDateTextPane.setEditable(false);
		noteDateTextPane.setBackground(SystemColor.controlHighlight);
		noteDateTextPane.setBounds(10, 16, 409, 24);
		dateBorderPanel.add(noteDateTextPane);
		
		descriptionBorderPanel = new JPanel();
		descriptionBorderPanel.setBorder(new TitledBorder(null, "Note Description", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		descriptionBorderPanel.setBounds(24, 187, 429, 194);
		noteViewPanel.add(descriptionBorderPanel);
		descriptionBorderPanel.setLayout(null);
		
		noteDescriptionTextPane = new JTextPane();
		noteDescriptionTextPane.setEditable(false);
		noteDescriptionTextPane.setBounds(10, 16, 301, 169);
		descriptionBorderPanel.add(noteDescriptionTextPane);
		noteDescriptionTextPane.setBackground(SystemColor.controlHighlight);
		
		noteTagScrollPane = new JScrollPane();
		noteTagScrollPane.setBounds(321, 16, 96, 169);
		descriptionBorderPanel.add(noteTagScrollPane);
		
		
		tagPanel = new JPanel();
		tagPanel.setBorder(new TitledBorder(null, "Add Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tagPanel.setBounds(24, 126, 130, 51);
		noteViewPanel.add(tagPanel);
		tagPanel.setLayout(null);
		
		noteTagTextField = new JTextField();
		noteTagTextField.setBounds(10, 16, 115, 24);
		tagPanel.add(noteTagTextField);
		noteTagTextField.setBackground(SystemColor.controlHighlight);
		noteTagTextField.setColumns(1);
		noteTagTextField.setEditable(false);
		
		btnAddTag = new JButton("Add Tag");
		btnAddTag.addActionListener(handler);
		btnAddTag.setBounds(164, 145, 89, 23);
		noteViewPanel.add(btnAddTag);
		btnAddTag.setEnabled(false);
		
		btnRemoveTag = new JButton("Remove Tag");
		btnRemoveTag.addActionListener(handler);
		btnRemoveTag.setBounds(345, 145, 93, 23);
		noteViewPanel.add(btnRemoveTag);
		btnRemoveTag.setEnabled(false);
		
		noteScrollPane = new JScrollPane();
		noteScrollPane.setBounds(510, 53, 173, 386);
		contentPane.add(noteScrollPane);
		
		tagModelList = new TagListModel(noteSystem);
		tagJList = new JList();
		tagJList.setModel(tagModelList);
		noteTagScrollPane.setViewportView(tagJList);
		tagJList.setBackground(SystemColor.controlHighlight);
		
		noteModelList = new NoteListModel(noteSystem);
		noteJList = new JList();
/*  	noteJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentNote = noteKeeper.loadNote(noteJList.getSelectedIndex());
			}
		});
*/		noteJList.setModel(noteModelList);
		noteScrollPane.setViewportView(noteJList);
		noteJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		noteJList.setBackground(SystemColor.control);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(699, 53, 105, 332);
		contentPane.add(tagScrollPane);
		
		currentTagModelList = new DefaultListModel<String>();
		currentTagJList = new JList();
		tagJList.setModel(currentTagModelList);
		tagScrollPane.setViewportView(currentTagJList);
		currentTagJList.setBackground(SystemColor.control);
		currentTagJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		updateFields();
		loadCurrentTags();
	}

	/**
	 * Launch the application.
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
	

	
	private void toggleEdit()
	{
		boolean editable = btnEdit.isEnabled();
		
		btnEdit.setEnabled					(editable ? false : true);
		btnSave.setEnabled					(editable ? true : false);
		btnAddTag.setEnabled				(editable ? true : false);
		btnRemoveTag.setEnabled				(editable ? true : false);
		noteTitleTextPane.setEditable		(editable ? true : false);
		noteDescriptionTextPane.setEditable	(editable ? true : false);
		noteTagTextField.setEditable		(editable ? true : false);
		
	}
	
	private void updateFields()
	{
		noteTitleTextPane.setText(currentNote.getTitle());
		noteDateTextPane.setText(currentNote.getDate());
		noteDescriptionTextPane.setText(currentNote.getDesc());
	}
	

	private void saveNote() 
	{
		currentNote.setTitle(noteTitleTextPane.getText());
		currentNote.setDesc(noteDescriptionTextPane.getText());
		currentNote.updateDate();	
	
		noteModelList.updateNote(currentNote);
	}
	
	private void loadCurrentTags()
	{
		ArrayList<String> currentTags = currentNote.getTags();
		
		currentTagModelList.removeAllElements();
		for (String t : currentTags)
			currentTagModelList.addElement(t);
	}
	/*
	public void loadNotes(ArrayList<Note> noteList)
	{
		noteModelList.removeAllElements();
		
		for (Note n : noteList)
			noteModelList.addElement(n);
	}
	
	public void loadTags(ArrayList<String> tagList)
	{
		tagModelList.removeAllElements();
		
		for (String t : tagList)
			tagModelList.addElement(t);
	}
	 */
}
