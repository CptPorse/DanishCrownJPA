package model;

import java.util.ArrayList;
import java.util.Date;

import service.Service;

//Author: Jens Nyberg Porse
public class LoadingBay
{

	private int loadingBayNumber;
	private boolean isloading;
	private ProductType productType;
	private Date nextAvailableTime;
	private ArrayList<LoadingInfo> loadingInfos;

	public LoadingBay(int loadingBayNumber, ProductType productType)
	{
		super();
		this.loadingBayNumber = loadingBayNumber;
		this.productType = productType;
		loadingInfos = new ArrayList<>();
	}

	public int getLoadingBayNumber()
	{
		return loadingBayNumber;
	}

	public void setLoadingBayNumber(int loadingBayNumber)
	{
		this.loadingBayNumber = loadingBayNumber;
	}

	public boolean isIsloading()
	{
		return isloading;
	}

	public void setIsloading(boolean isloading)
	{
		this.isloading = isloading;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public Date getNextAvailableTime()
	{
		return nextAvailableTime;
	}

	public void setNextAvailableTime(Date nextAvailableTime)
	{
		this.nextAvailableTime = nextAvailableTime;
	}

	/**
	 * Returns a list of LoadingInfo.
	 */
	public ArrayList<LoadingInfo> getLoadingInfos()
	{
		return new ArrayList<LoadingInfo>(loadingInfos);
	}

	/**
	 * Adds the LoadingInfo to the list.
	 */
	public void addLoadingInfo(LoadingInfo loadingInfo)
	{
		loadingInfos.add(loadingInfo);
	}

	/**
	 * Removes the LoadingInfo from the list.
	 */
	public void removeLoadingInfo(LoadingInfo loadingInfo)
	{
		loadingInfos.remove(loadingInfo);
	}

	/**
	 * A method Used to find the earliest time of which this LoadingBay is ready to load another SubOrder into a truck.
	 * @param earliestPacking: The earliest point in time, where the LoadingInfo in question is ready to get packed
	 * @return A Long variable with the wait time in milliseconds the LoadingInfo will have to wait to use this bay
	 * @Author Jens Nyberg Porse
	 */
	public Long getNextFreeTime(Date earliestLoadingTime)
	{

		Long waitTime;

		//If there is no Scheduled loading yet
		if (loadingInfos.isEmpty()) {
			nextAvailableTime = earliestLoadingTime;
			System.out.println("  No LoadingInfo scheduled. Ready at: "
					+ Service.getDateToStringTime(nextAvailableTime));
		}

		waitTime = nextAvailableTime.getTime() - earliestLoadingTime.getTime();
		if (waitTime < 0)
			waitTime = 0L;
		System.out.println("  LoadingBay: " + getLoadingBayNumber() + " is Ready at: "
				+ Service.getDateToStringTime(nextAvailableTime));
		return waitTime;
	}

	@Override
	public String toString()
	{
		return "Bay " + loadingBayNumber + " (" + productType + ")";
	}

}
