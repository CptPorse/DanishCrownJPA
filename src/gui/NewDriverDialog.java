package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import service.Service;

//Author: Jens Nyberg Porse
public class NewDriverDialog extends JDialog
{

	public NewDriverDialog(JFrame owner)
	{
		super(owner);
		System.out.println("Created new Window");
		setTitle("Create New Driver");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		InitContent();
	}

	private JPanel contentPanel;
	private JPanel buttonPane;
	private JTextField txfName;
	private JTextField txfPhoneNumber;
	private JTextField txfLicensPlate;
	private JLabel lblCreateNewDriver;
	private JLabel lblName;
	private JLabel lblPhoneNumber;
	private JLabel lblLicensePlate;
	private JButton btnRegister;
	private JButton btnCancel;

	private Controller controller;

	private void InitContent()
	{

		controller = new Controller();

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.LIGHT_GRAY);
		setBounds(100, 100, 202, 270);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lblCreateNewDriver = new JLabel("Create New Driver");
		lblCreateNewDriver.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCreateNewDriver.setBounds(10, 11, 152, 25);
		contentPanel.add(lblCreateNewDriver);

		lblName = new JLabel("Full Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setBounds(20, 47, 150, 14);
		contentPanel.add(lblName);

		txfName = new JTextField();
		txfName.setBounds(20, 65, 150, 20);
		contentPanel.add(txfName);
		txfName.setColumns(10);

		lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPhoneNumber.setBounds(20, 96, 150, 14);
		contentPanel.add(lblPhoneNumber);

		txfPhoneNumber = new JTextField();
		txfPhoneNumber.setBounds(20, 115, 150, 20);
		contentPanel.add(txfPhoneNumber);
		txfPhoneNumber.setColumns(10);

		lblLicensePlate = new JLabel("License Plate:");
		lblLicensePlate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLicensePlate.setBounds(20, 146, 150, 14);
		contentPanel.add(lblLicensePlate);

		txfLicensPlate = new JTextField();
		txfLicensPlate.setBounds(20, 165, 150, 20);
		contentPanel.add(txfLicensPlate);
		txfLicensPlate.setColumns(10);

		buttonPane = new JPanel();
		buttonPane.setBackground(Color.GRAY);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnRegister = new JButton("Register");
		btnRegister.setActionCommand("OK");
		buttonPane.add(btnRegister);
		btnRegister.addActionListener(controller);

		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
		btnCancel.addActionListener(controller);

	}

	private class Controller implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btnRegister)
			{
				//Error handling done by Christian M. Pedersen
				String errName = "", errPhone = "", errLicense = "";
				Boolean error = false;
				if (txfName.getText().trim().isEmpty())
				{
					errName = "Name\r\n";
					error = true;
				}
				if (txfPhoneNumber.getText().trim().isEmpty())
				{
					errPhone = "Phone\r\n";
					error = true;
				}
				if (txfLicensPlate.getText().trim().isEmpty())
				{
					errLicense = "License plate";
					error = true;
				}
				if (error = true)
				{
					//Error handle, if not all fields have been filled, display an error.
					JOptionPane.showMessageDialog(null, "One or more fields are missing input:\r\n" + errName
							+ errPhone + errLicense, "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					Service.createDriver(txfName.getText(), txfPhoneNumber.getText(), txfLicensPlate.getText());
					NewDriverDialog.this.setVisible(false);
				}
			}
			if (e.getSource() == btnCancel)
			{
				NewDriverDialog.this.setVisible(false);

			}

		}
	}
}
