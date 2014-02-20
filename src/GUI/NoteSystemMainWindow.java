package GUI;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import NoteKeeper.NoteKeeper;
import NoteSystem.Note;
import NoteSystem.Tag;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class NoteSystemMainWindow extends JFrame {

	private JPanel	contentPane;
	private DefaultListModel<Note> noteModelList;
	private DefaultListModel<Tag> tagModelList;
	private JList noteJList, tagJList;
	private NoteKeeper noteKeeper;
	private Note currentNote;
	private JScrollPane noteScrollPane;
	private JScrollPane tagScrollPane;
	private JTextField tagTextField;
	private JButton btnNewNote;
	private JButton btnNewButton;
	private JPanel noteViewPanel;
	private JPanel titelBorderPanel;
	private JTextPane noteDateTextPane;
	private JTextPane noteTitleTextPane;
	private JPanel descriptionBorderPanel;
	private JTextPane noteDescriptionTextPane;
	private JButton btnEdit;
	private JButton btnSave;
	private JTextField noteTagTextField;
	private JPanel tagPanel;
	private JButton btnAddTag;
	private JButton btnClose;
	private JPanel searchByTagBorder;
	private JLabel lblSmartwaterNotes;

	
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
			{

				btnEdit.setEnabled(false);
				btnSave.setEnabled(true);
				noteTitleTextPane.setEditable(true);
				noteDescriptionTextPane.setEditable(true);

			} else if (e.getSource().equals(btnRemoveSelected)) 
			{
				List<Book> selected = bookJList.getSelectedValuesList();

				for (Book book : selected) 
				{
					bookKeeperCntr.removeBook(book);
					
					bookModelList.removeElement(book);
				}
				
			}

		}

	}
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Create the frame.
	 */
	public NoteSystemMainWindow(NoteKeeper noteKeeper, Note note)
	{
		this.noteKeeper = noteKeeper;
		MainWindowButtonHandlr handler = new MainWindowButtonHandlr(this);
		currentNote = note;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 833, 519 );
		contentPane = new JPanel( );
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout(null);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(702, 53, 105, 386);
		contentPane.add(tagScrollPane);
		
		noteScrollPane = new JScrollPane();
		noteScrollPane.setBounds(510, 53, 173, 386);
		contentPane.add(noteScrollPane);
		
		/*
		noteModelList = new DefaultListModel<Note>();
		noteJList = new JList();
		noteJList.setModel(noteModelList);
		noteScrollPane.setViewportView(noteJList);
		
		tagModelList = new DefaultListModel<Tag>();
		tagJList = new JList();
		tagJList.setModel(tagModelList);
		tagScrollPane.setViewportView(noteJList);
		*/
		
		btnNewNote = new JButton("New Note");
		btnNewNote.setBounds(510, 19, 173, 23);
		contentPane.add(btnNewNote);
		
		btnNewButton = new JButton("Remove");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(510, 450, 173, 23);
		contentPane.add(btnNewButton);
		
		searchByTagBorder = new JPanel();
		searchByTagBorder.setBorder(new TitledBorder(null, "Search by Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchByTagBorder.setBounds(702, 0, 105, 43);
		contentPane.add(searchByTagBorder);
		searchByTagBorder.setLayout(null);
		
		tagTextField = new JTextField();
		tagTextField.setBounds(6, 16, 89, 20);
		tagTextField.setBackground(SystemColor.controlHighlight);
		searchByTagBorder.add(tagTextField);
		tagTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		tagTextField.setColumns(10);
		
		btnEdit = new JButton("Edit");
		btnEdit.setBounds(10, 20, 89, 23);
		contentPane.add(btnEdit);
		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.setBounds(10, 450, 89, 23);
		contentPane.add(btnSave);
		
		btnClose = new JButton("Save and Exit");
		btnClose.setBounds(702, 450, 105, 23);
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
		noteTitleTextPane.setBounds(6, 16, 413, 28);
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
		descriptionBorderPanel.setBounds(22, 187, 437, 194);
		noteViewPanel.add(descriptionBorderPanel);
		descriptionBorderPanel.setLayout(null);
		
		noteDescriptionTextPane = new JTextPane();
		noteDescriptionTextPane.setEditable(false);
		noteDescriptionTextPane.setBounds(6, 16, 425, 167);
		descriptionBorderPanel.add(noteDescriptionTextPane);
		noteDescriptionTextPane.setBackground(SystemColor.controlHighlight);
		
		tagPanel = new JPanel();
		tagPanel.setBorder(new TitledBorder(null, "Add Tag", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tagPanel.setBounds(24, 126, 138, 43);
		noteViewPanel.add(tagPanel);
		tagPanel.setLayout(null);
		
		noteTagTextField = new JTextField();
		noteTagTextField.setBounds(6, 16, 126, 20);
		tagPanel.add(noteTagTextField);
		noteTagTextField.setBackground(SystemColor.controlHighlight);
		noteTagTextField.setColumns(10);
		
		btnAddTag = new JButton("Add Tag");
		btnAddTag.setBounds(364, 138, 89, 23);
		noteViewPanel.add(btnAddTag);
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
	
	public void loadNotes(ArrayList<Note> notes)
	{
		for (Note n : notes)
			noteModelList.addElement(n);
	}
	
	public void loadTags(ArrayList<Tag> tags)
	{
		for (Tag t : tags)
			tagModelList.addElement(t);
	}
}
