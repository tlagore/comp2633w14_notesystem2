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

public class NoteSystemMainWindow extends JFrame {

	private JPanel	contentPane;
	private DefaultListModel<Note> noteModelList;
	private DefaultListModel<Tag> tagModelList;
	private JList noteJList, tagJList;
	private NoteKeeper noteKeeper;
	private Note currentNote;
	private JScrollPane noteScrollPane;
	private JScrollPane tagScrollPane;
	private JPanel DisplayWindowPanel;
	private JTextField tagTextField;
	private JButton btnNewNote;
	private JButton btnNewButton;

	
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

			if (e.getSource().equals(btnAddBook)) 
			{
			 	AddBookWindow aWindow = new AddBookWindow();
			 	
			 	aWindow.run(parent);

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
	public NoteSystemMainWindow(NoteKeeper noteKeeper)
	{
		this.noteKeeper = noteKeeper;
		MainWindowButtonHandlr handler = new MainWindowButtonHandlr(this);
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 833, 519 );
		contentPane = new JPanel( );
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout(null);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(702, 58, 91, 381);
		contentPane.add(tagScrollPane);
		
		noteScrollPane = new JScrollPane();
		noteScrollPane.setBounds(510, 58, 158, 381);
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
		btnNewNote.setBounds(505, 26, 163, 23);
		contentPane.add(btnNewNote);
		
		btnNewButton = new JButton("Remove");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(510, 450, 158, 23);
		contentPane.add(btnNewButton);
		
		DisplayWindowPanel = new JPanel();
		DisplayWindowPanel.setBounds(22, 58, 455, 381);
		contentPane.add(DisplayWindowPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		DisplayWindowPanel.add(scrollPane);
		
		tagTextField = new JTextField();
		tagTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		tagTextField.setBounds(702, 27, 91, 20);
		contentPane.add(tagTextField);
		tagTextField.setColumns(10);
		
	
		

		

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
