package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.ProductType;
import model.SubOrder;
import model.Trailer;
import model.TrailerState;
import service.Service;
import dao.Dao;

//Author: Jens Nyberg Porse
public class NewSubOrderDialog extends JDialog
{

	private Controller controller;
	private NewOrderDialog orderDialog;

	public NewSubOrderDialog(NewOrderDialog owner)
	{
		this.orderDialog = owner;
		setTitle("Create Sub-Order");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(false);
		InitContent();
	}

	public NewSubOrderDialog GetInstance()
	{
		return this;
	}

	private JPanel contentPanel;
	private JPanel buttonPane;
	private JButton btnCreate, btnCancel;
	private JLabel lblNewOrder, lblProductType, lblTrailer, lblSubWeight;
	private JTextField txfWeight;
	private JComboBox<ProductType> cmbProductType;
	private JComboBox<Trailer> cmbTrailer;

	private void InitContent()
	{

		controller = new Controller();

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.LIGHT_GRAY);
		setBounds(100, 100, 240, 290);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lblNewOrder = new JLabel("Create New Sub-Order");
		lblNewOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewOrder.setBounds(10, 11, 173, 25);
		contentPanel.add(lblNewOrder);

		lblTrailer = new JLabel("Trailer:");
		lblTrailer.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTrailer.setBounds(20, 146, 152, 14);
		contentPanel.add(lblTrailer);

		cmbTrailer = new JComboBox<Trailer>();
		cmbTrailer.setBounds(20, 165, 191, 40);
		contentPanel.add(cmbTrailer);
		cmbTrailer.addActionListener(controller);

		lblProductType = new JLabel("Product Type");
		lblProductType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProductType.setBounds(20, 96, 152, 14);
		contentPanel.add(lblProductType);

		cmbProductType = new JComboBox<ProductType>();
		cmbProductType.setBounds(20, 115, 191, 20);
		contentPanel.add(cmbProductType);
		cmbProductType.addActionListener(controller);

		lblSubWeight = new JLabel("Estimated Weight");
		lblSubWeight.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSubWeight.setBounds(20, 47, 150, 14);
		contentPanel.add(lblSubWeight);

		txfWeight = new JTextField("0");
		txfWeight.setColumns(10);
		txfWeight.setBounds(20, 65, 75, 20);
		contentPanel.add(txfWeight);
		txfWeight.addFocusListener(controller); // Christian

		JLabel lblTons = new JLabel("Kilo");
		lblTons.setBounds(97, 68, 46, 14);
		contentPanel.add(lblTons);

		buttonPane = new JPanel();
		buttonPane.setBackground(Color.GRAY);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnCreate = new JButton("Create");
		btnCreate.setActionCommand("OK");
		buttonPane.add(btnCreate);
		getRootPane().setDefaultButton(btnCreate);
		btnCreate.addActionListener(controller);

		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
		btnCancel.addActionListener(controller);

		controller.fillCmbProductType();
	}

	private class Controller implements ActionListener, FocusListener
	{

		private void fillCmbProductType()
		{
			DefaultComboBoxModel<ProductType> cmbProductTypeModel = new DefaultComboBoxModel<ProductType>(Dao.getProductTypes().toArray(new ProductType[0]));
			cmbProductType.setModel(cmbProductTypeModel);
			cmbProductType.setSelectedItem(null);
		}

		private void fillCmbTrailer()
		{
			ArrayList<Trailer> avalibleTrailers = new ArrayList<Trailer>();
			for (int i = 0; i < Dao.getTrailer().size(); i++)
			{
				Trailer t1 = Dao.getTrailer().get(i);
				if (t1.getProductTypes().contains(cmbProductType.getSelectedItem()) == true
						&& t1.getWeightCurrent() + Double.parseDouble(txfWeight.getText()) < t1.getWeightMax() && t1.getTrailerState() == TrailerState.IDLE)
				{
					avalibleTrailers.add(Dao.getTrailer().get(i));
					System.out.println("Added " + Dao.getTrailer().get(i));
				}
			}

			DefaultComboBoxModel<Trailer> cmbTrailerModel = new DefaultComboBoxModel<Trailer>(avalibleTrailers.toArray(new Trailer[0]));
			cmbTrailer.setModel(cmbTrailerModel);
			System.out.println(avalibleTrailers);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource() == btnCreate)
			{
				//Error handling done by Christian M. Pedersen
				String errTrailer = "", errProduct = "", errWeight = "";
				Boolean error = false;
				if (cmbTrailer.getSelectedItem() == null)
				{
					errTrailer = "Trailer\r\n";
					error = true;
				}
				if (cmbProductType.getSelectedItem() == null)
				{
					errProduct = "Product\r\n";
					error = true;
				}
				if (txfWeight.getText().trim().isEmpty())
				{
					errWeight = "Weight\r\n";
					error = true;
				}

				if (Integer.parseInt(txfWeight.getText()) == 0)
				{
					errWeight = "Weight (Enter a number [1-25000])\r\n";
					error = true;
				}
				if (error == true)
				{
					//Error handle, if not all fields have been filled, display an error.
					JOptionPane.showMessageDialog(null, "One or more fields require input or selection:\r\n" + errWeight + errProduct + errTrailer, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					Trailer t1 = cmbTrailer.getItemAt(cmbTrailer.getSelectedIndex());

					ProductType p1 = cmbProductType.getItemAt(cmbProductType.getSelectedIndex());

					double weight = Double.parseDouble(txfWeight.getText());

					SubOrder s1 = Service.createSubOrder(weight, t1, p1);

					t1.setWeightCurrent(t1.getWeightCurrent() + (Double.parseDouble(txfWeight.getText())));
					orderDialog.addSubOrder(s1);
					NewSubOrderDialog.this.dispose();
				}
			}

			if (e.getSource() == btnCancel)
			{
				NewSubOrderDialog.this.dispose();
			}

			if (e.getSource() == cmbProductType)
			{
				if (!txfWeight.getText().equals("0"))
				{
					fillCmbTrailer();
					System.out.println("Clicked the combobox");
				}
			}

		}

// Author: Christian M. Pedersen
		@Override
		public void focusGained(FocusEvent e)
		{
			// Unused
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			if (e.getSource() == txfWeight)
			{
				try
				{
					int weight = Integer.parseInt(txfWeight.getText());
					if (weight < 0)
					{
						txfWeight.setText("0");
						//Error handle, if weight is a negative number, display an error.
						JOptionPane.showMessageDialog(null, "Weight must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException nfe)
				{
					txfWeight.setText("0");
					//Error handle, if weight is not a number, display an error.
					JOptionPane.showMessageDialog(null, "Weight must be a number", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
	}
}
