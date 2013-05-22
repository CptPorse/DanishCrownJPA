package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.LoadingBay;
import model.Order;
import model.SubOrder;
import model.TrailerState;
import service.Service;
import dao.Dao;
import dateutil.DU;

//Author: Jens Nyberg Porse
public class NewOrderDialog extends JDialog
{

	private static NewOrderDialog orderFrame;
	private ExternalSystemView externalSystemView;

	public NewOrderDialog(ExternalSystemView owner)
	{
		this.externalSystemView = owner;
		System.out.println("Created new Window");
		setTitle("Create Order");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(false);
		orderFrame = this;
		InitContent();
	}

	private JPanel contentPanel;
	private JPanel buttonPane;
	private JButton btnCreate, btnCancel;
	private JLabel lblNewOrder;
	private JLabel lblOrderID;
	public JTextField txfOrderID;
	private JLabel lblWeightMargin;
	private JTextField txfWeightMargin;
	private JLabel lblSubOrders;
	private JList<SubOrder> lstSubOrders;
	private DefaultListModel lstSubOrderModel;
	private JLabel lblKg;
	private JLabel lblLoadingDate;
	private JTextField txfLoadingDate;
	private JButton btnAddSuborder;

	private Controller controller;
	private ArrayList<SubOrder> subOrders;

	public static NewOrderDialog getInstance()
	{
		return orderFrame;
	}

	private void InitContent()
	{

		controller = new Controller();
		subOrders = new ArrayList<SubOrder>();

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.LIGHT_GRAY);
		setBounds(100, 100, 484, 270);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lblNewOrder = new JLabel("Create New Order");
		lblNewOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewOrder.setBounds(10, 11, 152, 25);
		contentPanel.add(lblNewOrder);

		lblOrderID = new JLabel("Order ID:");
		lblOrderID.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOrderID.setBounds(20, 47, 150, 14);
		contentPanel.add(lblOrderID);

		txfOrderID = new JTextField();
		txfOrderID.setColumns(10);
		txfOrderID.setBounds(20, 65, 150, 20);
		contentPanel.add(txfOrderID);

		lblWeightMargin = new JLabel("Weight Margin:");
		lblWeightMargin.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWeightMargin.setBounds(20, 96, 150, 14);
		contentPanel.add(lblWeightMargin);

		txfWeightMargin = new JTextField();
		txfWeightMargin.setColumns(10);
		txfWeightMargin.setBounds(20, 115, 45, 20);
		contentPanel.add(txfWeightMargin);

		lblSubOrders = new JLabel("Sub Orders");
		lblSubOrders.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSubOrders.setBounds(204, 47, 150, 14);
		contentPanel.add(lblSubOrders);

		lstSubOrderModel = new DefaultListModel<SubOrder>();
		lstSubOrders = new JList<SubOrder>(lstSubOrderModel);
		lstSubOrders.setBounds(204, 65, 254, 90);
		contentPanel.add(lstSubOrders);

		lblKg = new JLabel("Kilo");
		lblKg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblKg.setBounds(69, 118, 46, 14);
		contentPanel.add(lblKg);

		lblLoadingDate = new JLabel("Loading Date;");
		lblLoadingDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLoadingDate.setBounds(20, 145, 150, 14);
		contentPanel.add(lblLoadingDate);

		txfLoadingDate = new JTextField("2013-01-01");
		txfLoadingDate.setEditable(false);
		txfLoadingDate.setColumns(10);
		txfLoadingDate.setBounds(20, 165, 150, 20);
		contentPanel.add(txfLoadingDate);

		btnAddSuborder = new JButton("Add Sub-Order");
		btnAddSuborder.setBounds(204, 166, 150, 23);
		contentPanel.add(btnAddSuborder);
		btnAddSuborder.addActionListener(controller);

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
	}

	public void addSubOrder(SubOrder subOrder)
	{
		subOrders.add(subOrder);
		System.out.println("Added: " + subOrder);
		controller.fillList();
	}

	private class Controller implements ActionListener
	{

		private void fillList()
		{
			SubOrder[] arraySubOrder = subOrders.toArray(new SubOrder[0]);
			lstSubOrders.setListData(arraySubOrder);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource() == btnAddSuborder)
			{
				NewSubOrderDialog createSubOrderDialog = new NewSubOrderDialog(orderFrame);
				createSubOrderDialog.setVisible(true);

			}

			if (e.getSource() == btnCreate)
			{

				//Error handling done by Christian M. Pedersen
				String errDate = "", errID = "", errSubOrders = "";
				Boolean error = false;
				if (txfLoadingDate.getText().trim().isEmpty())
				{
					errDate = "Date\r\n";
					error = true;
				}
				if (lstSubOrders.isSelectionEmpty())
				{
					errSubOrders = "SubOrder\r\n";
					error = true;
				}
				if (txfOrderID.getText().trim().isEmpty())
				{
					errID = "Trailer ID\r\n";
					error = true;
				}
				if (error == true)
				{
					//Error handle, if not all fields have been filled, display an error.
					JOptionPane.showMessageDialog(null, "One or more fields require input or selection:\r\n" + errDate + errID + errSubOrders, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{

					Date loadingDate = DU.createDate(txfLoadingDate.getText());
					Order o = Service.createOrder(Integer.parseInt(txfOrderID.getText()), loadingDate);
					if (!txfWeightMargin.getText().isEmpty() == true)
					{
						o.setWeightMarginKilo(Double.parseDouble(txfWeightMargin.getText()));
					}

					for (int i = 0; i < lstSubOrders.getModel().getSize(); i++)
					{
						SubOrder subOrder = lstSubOrders.getModel().getElementAt(i);
						o.addSubOrder(subOrder);
						subOrder.setOrder(o);
						System.out.println(subOrder);
						Service.setSubOrderEarliestLoadingTime(subOrder.getTrailer());
						LoadingBay lb = Dao.getLoadingBays().get(0);
						for (LoadingBay loadingBay : Dao.getLoadingBays())
						{
							if (loadingBay.getProductType() == subOrder.getProductType())
								lb = loadingBay;
						}
						Service.createLoadingInfo(subOrder, lb);
						System.out.println(lb.getLoadingInfos());
						subOrder.getLoadingInfo().setLoadingBay(lb);

						Service.refreshLoadingBays(subOrder.getProductType());
						LoadingBayView.fillInfo(null);
						TrailerView.fillModel(TrailerState.ENROUTE);
					}

					externalSystemView.updateLstOrder();
					System.out.println("I send this Update");
					NewOrderDialog.this.dispose();
				}
			}
			if (e.getSource() == btnCancel)
			{
				for (SubOrder subOrder : subOrders)
				{
					Dao.removeSubOrder(subOrder);
				}
				NewOrderDialog.this.dispose();
			}

		}
	}
}
