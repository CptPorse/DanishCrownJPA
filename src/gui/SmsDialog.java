package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import model.Driver;
import model.Trailer;

//Author Christian "sexy" Møller Pedersen
public class SmsDialog extends JFrame
{
	private Controller controller;

	public SmsDialog(Trailer trailer)
	{
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(600, 375, 350, 300);
		this.setTitle("Sexy SMS 1.0");
		InitContent(trailer);
		this.setVisible(true);
	}

	private JPanel contentPane;
	private JLabel lblReceiver, lblMessage;
	private JTextArea txaReceiver, txaMessage;
	private JButton btnOk;

	private void InitContent(Trailer trailer)
	{
		controller = new Controller();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblReceiver = new JLabel("Receiver:");
		lblReceiver.setBounds(5, 5, 60, 24);
		contentPane.add(lblReceiver);

		txaReceiver = new JTextArea();
		txaReceiver.setBounds(70, 5, 270, 24);
		txaReceiver.setEditable(false);
		contentPane.add(txaReceiver);

		lblMessage = new JLabel("Message:");
		lblMessage.setBounds(5, 44, 60, 24);
		contentPane.add(lblMessage);

		txaMessage = new JTextArea();
		txaMessage.setBounds(5, 73, 335, 160);
		txaMessage.setEditable(false);
		contentPane.add(txaMessage);

		btnOk = new JButton("OK");
		btnOk.setBounds(145, 240, 60, 25);
		btnOk.addActionListener(controller);
		contentPane.add(btnOk);

		//Receiver
		Driver driver = trailer.getDriver();
		if (driver != null)
		{
			txaReceiver.setText(driver.getName() + " (" + driver.getPhoneNumber() + ")");
			txaMessage.setText("Hi " + driver.getName() + ".\n\rYour trailer (" + trailer.getTrailerID()
					+ ") is now finished loading.\n\rPlease pick it up at loading bay " + trailer.getLoadingBay() + ".\n\rRegards\n\rDanish Crown");
		}

	}

	private class Controller implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (arg0.getSource() == btnOk)
			{
				((JFrame)btnOk.getTopLevelAncestor()).dispose();
			}
		}
	}
}
