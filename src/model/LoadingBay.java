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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.jdt.annotation.NonNullByDefault;

import service.Service;
import dao.Dao;

//Author: Jens Nyberg Porse
@NonNullByDefault
@Entity
@Table(name = "Loadingbay")
public class LoadingBay
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loadingbay_id")
	private long id;
	private int loadingBayNumber;
	private boolean isloading;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date nextAvailableTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "producttype_id")
	private ProductType productType;

	@OneToMany(mappedBy = "loadingBay", cascade = CascadeType.ALL)
	private List<LoadingInfo> loadingInfos;

	public LoadingBay()
	{

	}

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
		Dao.updateDatabase(this);
	}

	public boolean isIsloading()
	{
		return isloading;
	}

	public void setIsloading(boolean isloading)
	{
		this.isloading = isloading;
		Dao.updateDatabase(this);
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
		Dao.updateDatabase(this);
	}

	public Date getNextAvailableTime()
	{
		return nextAvailableTime;
	}

	public void setNextAvailableTime(Date nextAvailableTime)
	{
		this.nextAvailableTime = nextAvailableTime;
		Dao.updateDatabase(this);
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
		Dao.updateDatabase(this);
	}

	/**
	 * Removes the LoadingInfo from the list.
	 */
	public void removeLoadingInfo(LoadingInfo loadingInfo)
	{
		loadingInfos.remove(loadingInfo);
		Dao.updateDatabase(this);
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
