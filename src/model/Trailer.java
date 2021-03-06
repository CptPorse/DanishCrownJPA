package model;

import gui.SmsDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import service.Service;
import dao.Dao;

@NonNullByDefault
@Entity
@Table(name = "Trailer")
public class Trailer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trailer_id")
	private long id;
	private String trailerID;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date timeOfArrival;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date timeOfDeparture;
	private double weightCurrent;
	private double weightMax;

	@Enumerated(EnumType.STRING)
	private TrailerState trailerState;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loadingbay_id")
	private LoadingBay loadingBay;

	@OneToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;

	@ManyToMany
	@JoinTable(name = "Trailer_Producttype", joinColumns = @JoinColumn(name = "trailer_id"), inverseJoinColumns = @JoinColumn(name = "producttype_id"))
	private List<ProductType> productTypes = new ArrayList<ProductType>();

	@OneToMany(mappedBy = "trailer", cascade = CascadeType.ALL)
	private List<SubOrder> subOrders = new ArrayList<SubOrder>();

	public Trailer()
	{

	}

	public Trailer(String trailerID, double weightMax, Date timeOfArrival)
	{
		super();
		this.timeOfArrival = timeOfArrival;
		this.trailerID = trailerID;
		this.weightMax = weightMax;
		this.trailerState = TrailerState.IDLE;

	}

	public TrailerState getTrailerState()
	{
		return trailerState;
	}

	public void setTrailerState(TrailerState trailerState)
	{
		this.trailerState = trailerState;
		Dao.updateDatabase(this);
	}

	public String getTrailerID()
	{
		return trailerID;
	}

	public void setTrailerID(String trailerID)
	{
		this.trailerID = trailerID;
		Dao.updateDatabase(this);
	}

	public Date getTimeOfArrival()
	{
		return timeOfArrival;
	}

	public void setTimeOfArrival(Date timeOfArrival)
	{
		this.timeOfArrival = timeOfArrival;
		Dao.updateDatabase(this);
	}

	@Nullable
	public Date getTimeOfDeparture()
	{
		return timeOfDeparture;
	}

	public void setTimeOfDeparture(Date timeOfDeparture)
	{
		this.timeOfDeparture = timeOfDeparture;
		Dao.updateDatabase(this);
	}

	public double getWeightCurrent()
	{
		return weightCurrent;
	}

	public void setWeightCurrent(double weightCurrent)
	{
		this.weightCurrent = weightCurrent;
		Dao.updateDatabase(this);
	}

	public double getWeightMax()
	{
		return weightMax;
	}

	public void setWeightMax(double weightMax)
	{
		this.weightMax = weightMax;
		Dao.updateDatabase(this);
	}

	@Nullable
	public Driver getDriver()
	{
		return driver;
	}

	public void setDriver(Driver driver)
	{
		this.driver = driver;
		Dao.updateDatabase(this);
	}

	public void clearDriver()
	{
		this.driver = null;
		Dao.updateDatabase(this);
	}

	/**
	* @return the loadingBay the trailer is currently parked at.
	*/
	public LoadingBay getLoadingBay()
	{
		return loadingBay;
	}

	/**
	 * @param loadingBay the loadingBay the trailer is currently parked at.
	 */
	public void setLoadingBay(@Nullable LoadingBay loadingBay)
	{
		this.loadingBay = loadingBay;
		Dao.updateDatabase(this);
	}

	public void clearLoadingBay()
	{
		this.loadingBay = null;
		Dao.updateDatabase(this);
	}

	/**
	 * Returns a list of productTypes in this Trailer.
	 */
	public ArrayList<ProductType> getProductTypes()
	{
		return new ArrayList<ProductType>(productTypes);
	}

	/**
	 * Adds the productType to this Trailer.
	 */
	public void addProductType(ProductType productType)
	{
		productTypes.add(productType);
		Dao.updateDatabase(this);
	}

	/**
	 * Removes the productType from this Trailer.
	 */
	public void removeProductType(ProductType productType)
	{
		productTypes.remove(productType);
		Dao.updateDatabase(this);
	}

	/**
	 * Returns a list of productTypes in this Trailer.
	 */
	public ArrayList<SubOrder> getSubOrders()
	{
		return new ArrayList<SubOrder>(subOrders);
	}

	/**
	 * Adds the productType to this Trailer.
	 */
	public void addSubOrder(SubOrder subOrder)
	{
		subOrders.add(subOrder);
		//Updates the current weight of the trailer
		weightCurrent += subOrder.getEstimatedWeight();
		Dao.updateDatabase(this);
	}

	/**
	 * Removes the productType from this Trailer.
	 */
	public void removeSubOrder(SubOrder subOrder)
	{
		subOrders.remove(subOrder);
		Dao.updateDatabase(this);
	}

	public void registerArrival()
	{
		setTrailerState(TrailerState.ARRIVED);
		getSubOrders().get(0).getLoadingInfo().setState(LoadingInfoState.READY_TO_LOAD);
	}

	/**
	 * Method used when loading of a suborder is commenced.
	 * @param loadingInfo: The loadingInfo which is associated with the suborder being loaded
	 * @author S�ren M�ller Nielsen
	 */
	public void beginLoading(LoadingInfo loadingInfo)
	{
		setTrailerState(TrailerState.BEING_LOADED);
		setLoadingBay(loadingInfo.getLoadingBay());
	}

	/**
	 * Method used then loading of a suborder is completed.
	 * @param loadingInfo: The loadingInfo which is associated with the suborder being loaded
	 * @author S�ren M�ller Nielsen
	 */
	public void endLoading(LoadingInfo loadingInfo)
	{
		ArrayList<SubOrder> subOrders = loadingInfo.getSubOrder().getTrailer().getSubOrders();
		// searches if any of the attached sub orders to the trailer, aren't done loading, if any, it will set their LoadingInfoStat
		boolean trailerFullyLoaded = true;
		boolean changed = false;
		for (SubOrder s : subOrders) {
			if (s.isLoaded() == false) {
				if (changed == false) {
					s.getLoadingInfo().setState(LoadingInfoState.READY_TO_LOAD);
					changed = true;
				}
				trailerFullyLoaded = false;
			}
		}
		// if all the sub orders are done, trailer changes trailerstate to: loaded
		if (trailerFullyLoaded == true) {
			setTrailerState(TrailerState.LOADED);
			setTimeOfDeparture(loadingInfo.getTimeOfLoadingEnd());
			setLoadingBay(loadingInfo.getLoadingBay());
			SmsDialog sms = new SmsDialog(this);
		} else {
			setTrailerState(TrailerState.ARRIVED);
		}
	}

	public void registerDeparture()
	{
		setTrailerState(TrailerState.DEPARTED);
		setLoadingBay(null);
	}

	public void repackTrailer()
	{
		setTrailerState(TrailerState.ARRIVED);
		for (SubOrder subOrder : getSubOrders()) {
			System.out.println("Looping. Looking at: " + subOrder);

			subOrder.setEarliestLoadingTime(subOrder.getLoadingInfo().getTimeOfLoadingEnd());
			System.out.println("Setting " + subOrder + " earliestLoadingTime to: "
					+ subOrder.getLoadingInfo().getTimeOfLoadingEnd());
			LoadingInfo newLoadingInfo = Service.createLoadingInfo(subOrder, subOrder
					.getLoadingInfo().getLoadingBay());
			newLoadingInfo.setState(LoadingInfoState.READY_TO_LOAD);
			subOrder.setHighPriority(true);
			Service.refreshLoadingBays(subOrder.getProductType());
		}

	}

	//Author: Christian M�ller Pedersen
	@Override
	public String toString()
	{
		String string = null;
		switch (trailerState) {
		case ARRIVED:
			string = "<html><table>Trailer: " + trailerID + "<br>" + subOrders.get(0).getOrder();
			break;
		case BEING_LOADED:
			LoadingBay lb = null;
			for (LoadingInfo li : Dao.getLoadingInfos()) {
				if (li.getSubOrder().getTrailer() == this
						&& li.getSubOrder().getLoadingInfo().getState() == LoadingInfoState.LOADING) {
					lb = li.getLoadingBay();
				}
			}
			string = "<html><table>Trailer: " + trailerID + "<br>" + lb;
			break;
		case DEPARTED:
			string = "<html><table>Trailer: " + trailerID + "<br>Departed: "
					+ Service.getDateToStringTime(timeOfDeparture);
			break;
		case ENROUTE:
			string = "<html><table>Trailer: " + trailerID + "<br>ETA: "
					+ Service.getDateToStringTime(timeOfArrival);
			break;
		case LOADED:
			string = "<html><table>Trailer: " + trailerID + "<br>Weight: " + weightCurrent
					+ " kg<br>Max: " + weightMax + " kg";
			break;
		default:
			string = "<html><table>Trailer: " + trailerID + "<br>ETA: "
					+ Service.getDateToStringTime(timeOfArrival);
			break;

		}
		return string;
	}
}
