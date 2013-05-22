package model;

import java.util.Date;

public class SubOrder
{

	private double estimatedWeight;
	private int estimatedLoadingTime;
	private boolean isLoaded = false;
	private boolean highPriority = false;
	private Date earliestLoadingTime;
	private Order order;
	private Trailer trailer;
	private ProductType productType;
	private LoadingInfo loadingInfo;

	public SubOrder(double estimatedWeight, Trailer trailer, ProductType productType)
	{
		super();
		this.estimatedWeight = estimatedWeight;
		this.trailer = trailer;
		this.productType = productType;
		this.estimatedLoadingTime = (int)(this.productType.getminuteToKiloRatio() * this.estimatedWeight);
	}

	public boolean isLoaded()
	{
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded)
	{
		this.isLoaded = isLoaded;
	}

	public boolean isHighPriority()
	{
		return highPriority;
	}

	public void setHighPriority(boolean highPriority)
	{
		this.highPriority = highPriority;
	}

	public double getEstimatedWeight()
	{
		return estimatedWeight;
	}

	public void setEstimatedWeight(double estimatedWeight)
	{
		this.estimatedWeight = estimatedWeight;
	}

	public int getEstimatedLoadingTime()
	{
		return estimatedLoadingTime;
	}

	public void setEstimatedLoadingTime(int estimatedLoadingTime)
	{
		this.estimatedLoadingTime = estimatedLoadingTime;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public Trailer getTrailer()
	{
		return trailer;
	}

	public void setTrailer(Trailer trailer)
	{
		this.trailer = trailer;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public Date getEarliestLoadingTime()
	{
		return earliestLoadingTime;
	}

	public void setEarliestLoadingTime(Date earliestLoadingTime)
	{
		this.earliestLoadingTime = earliestLoadingTime;
	}

	public LoadingInfo getLoadingInfo()
	{
		return loadingInfo;
	}

	public void setLoadingInfo(LoadingInfo loadingInfo)
	{
		this.loadingInfo = loadingInfo;
	}

	@Override
	public String toString()
	{
		if (getOrder() == null) {
			return estimatedWeight + "kg of " + productType;
		} else
			return "Suborder of order nr: " + order.getOrderNumber();
	}
}
