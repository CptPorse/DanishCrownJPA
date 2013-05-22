package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
@Entity
@Table(name = "\"Order\"")
public class Order
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private long id;
	private int orderNumber;
	private double weightMarginPercent = 2;
	private double weightMarginKilo;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date loadingDate;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<SubOrder> subOrders = new ArrayList<SubOrder>();

	public Order()
	{

	}

	public Order(int orderNumber, Date loadingDate)
	{
		super();
		this.orderNumber = orderNumber;
		this.loadingDate = loadingDate;
	}

	public int getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public double getWeightMarginPercent()
	{
		return weightMarginPercent;
	}

	public void setWeightMarginPercent(double weightMarginPercent)
	{
		this.weightMarginPercent = weightMarginPercent;
	}

	public double getWeightMarginKilo()
	{
		return weightMarginKilo;
	}

	public void setWeightMarginKilo(double weightMarginKilo)
	{
		this.weightMarginKilo = weightMarginKilo;
	}

	public Date getLoadingDate()
	{
		return loadingDate;
	}

	public void setLoadingDate(Date loadingDate)
	{
		this.loadingDate = loadingDate;
	}

	/**
	 * Returns a list of sub-orders in this Order.
	 */
	public ArrayList<SubOrder> getSubOrders()
	{
		return new ArrayList<SubOrder>(subOrders);
	}

	/**
	 * Adds the sub-order to this Order.
	 */
	public void addSubOrder(SubOrder subOrder)
	{
		subOrders.add(subOrder);
		subOrder.setOrder(this);
		weightMarginKilo = calculateWeightMargin();
		subOrder.getTrailer().setTrailerState(TrailerState.ENROUTE);
	}

	/**
	 * Removes the sub-order from this Order.
	 */
	public void removeSubOrder(SubOrder subOrder)
	{
		subOrders.remove(subOrder);
	}

	private double calculateWeightMargin()
	{
		double totalWeight = 0;
		for (SubOrder subOrder : subOrders) {
			totalWeight += subOrder.getEstimatedWeight();
		}
		return totalWeight * ((weightMarginPercent / 100));
	}

	@Override
	public String toString()
	{
		return "Order ID: " + orderNumber;
	}

}
