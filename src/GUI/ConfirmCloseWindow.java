package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfirmCloseWindow extends JFrame {

	private JPanel contentPane;
	private NoteSystemMainWindow mWindow;
	private JButton btnNope;
	private JButton btnYes;

	
	
	public class CloseWindowButtonHandlr implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(btnNope))
			{
				closeWindow();
			}else if (e.getSource().equals(btnYes))
			{
				closeWindow();
				mWindow.exitAndSave();
			}
			
		}
	}
	
	
	
	
	/**
	 * Create the frame.
	 */
	public ConfirmCloseWindow() {
		
		CloseWindowButtonHandlr handler = new CloseWindowButtonHandlr();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) 
			{
				closeWindow();
			}
		});
		
		setBounds(100, 100, 315, 134);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAreYouSure = new JLabel("Are you sure you want to exit?");
		lblAreYouSure.setBounds(10, 11, 207, 14);
		contentPane.add(lblAreYouSure);
		
		btnYes = new JButton("Yes");
		btnYes.addActionListener(handler);
		btnYes.setBounds(227, 7, 62, 23);
		contentPane.add(btnYes);
		
		btnNope = new JButton("NOPE NOTES R KEWL");
		btnNope.addActionListener(handler);
		btnNope.setBounds(10, 36, 279, 49);
		contentPane.add(btnNope);
		
	}
	
	private void closeWindow()
	{
		setVisible(false);
		mWindow.confirmCloseWindowHasClosed( );
		dispose();
	}
	
	public void run(NoteSystemMainWindow mWindow) 
	{
		this.mWindow = mWindow;
		
		Dimension mWDimension = mWindow.getSize( );
		Point loc = mWindow.getLocation( );
		
		setLocation((int)(loc.getX( ) + mWDimension.getWidth( ) / 2 - this.getSize().getWidth() / 2) 
				, (int)(loc.getY( ) + mWDimension.getHeight() / 2 - this.getSize().getHeight() / 2));
		
		setVisible(true);
		
	}
}
