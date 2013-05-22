package service;

import java.util.ArrayList;
import java.util.Date;

import model.Driver;
import model.LoadingBay;
import model.LoadingInfo;
import model.LoadingInfoState;
import model.Order;
import model.ProductType;
import model.SubOrder;
import model.Trailer;
import model.TrailerState;
import dao.Dao;

public class Service
{

	//A number of methods that calls the constructor method of each class in the model package to create new objects. Proper linking are made where needed.

	public static Driver createDriver(String name, String phoneNumber, String licensePlate)
	{
		Driver driver = new Driver(name, phoneNumber, licensePlate);
		Dao.addDriver(driver);
		return driver;
	}

	public static Trailer createTrailer(String trailerID, double weightMax, Date timeOfArrival)
	{
		Trailer trailer = new Trailer(trailerID, weightMax, timeOfArrival);
		Dao.addTrailer(trailer);
		return trailer;
	}

	public static ProductType createProductType(String description, double minuteToKiloRatio)
	{
		ProductType productType = new ProductType(description, minuteToKiloRatio);
		Dao.addProductType(productType);
		return productType;
	}

	public static Order createOrder(int orderNumber, Date loadingDate)
	{
		Order order = new Order(orderNumber, loadingDate);
		Dao.addOrder(order);
		return order;
	}

	public static SubOrder createSubOrder(double estimatedWeight, Trailer trailer,
			ProductType productType)
	{
		SubOrder subOrder = new SubOrder(estimatedWeight, trailer, productType);
		trailer.addSubOrder(subOrder);
		Dao.addSubOrder(subOrder);
		trailer.setTrailerState(TrailerState.ENROUTE);
		return subOrder;
	}

	public static LoadingBay createLoadingBay(int loadingBayNumber, ProductType productType)
	{
		LoadingBay loadingBay = new LoadingBay(loadingBayNumber, productType);
		Dao.addLoadingBay(loadingBay);
		return loadingBay;
	}

	public static LoadingInfo createLoadingInfo(SubOrder subOrder, LoadingBay loadingBay)
	{
		LoadingInfo loadingInfo = new LoadingInfo(subOrder, loadingBay);
		Dao.addLoadingInfo(loadingInfo);
		loadingBay.addLoadingInfo(loadingInfo);
		subOrder.setLoadingInfo(loadingInfo);
		return loadingInfo;
	}

	/**
	 * A method that sorts the ArrayList<Trailers> from the Dao-layer using the booblesort algorithm. Trailers with the earliest timeOfArrival gets placed first in the Daos ArrayList<Trailer>.
	 * @author Jens Nyberg Porse
	 */
	public static void sortTrailerArrival()
	{

		ArrayList<Trailer> trailers = new ArrayList<Trailer>();
		trailers.addAll(Dao.getTrailer());

		int position, scan;
		for (position = trailers.size() - 1; position >= 0; position--) {
			for (scan = 0; scan <= position - 1; scan++) {
				if (trailers.get(scan).getTimeOfArrival()
						.after((trailers.get(scan + 1).getTimeOfArrival())))
					swap(trailers, scan, scan + 1);
			}
		}
		for (int i = trailers.size() - 1; i >= 0; i--) {
			Dao.removeTrailer(Dao.getTrailer().get(i));
		}
		for (int i = 0; i < trailers.size(); i++) {
			Dao.addTrailer(trailers.get(i));
		}
	}

	/**
	 * A method that sorts an ArrayList<SubOrders> using the booblesort algorithm
	 * @param subOrders: Any ArrayList<SubOrder> 
	 * @return An ArrayList<SubOrder> that is sorted based on the subOrders earlietsLoadingTime parameter. If there is any subOrders with high priority, they get first in line.
	 * @author Jens Nyberg Porse
	 */
	public static ArrayList<SubOrder> sortSubOrders(ArrayList<SubOrder> subOrders)
	{

		ArrayList<SubOrder> subOrdersSorted = subOrders;
		int position, scan;
		for (position = subOrdersSorted.size() - 1; position >= 0; position--) {
			for (scan = 0; scan <= position - 1; scan++) {
				if (subOrdersSorted.get(scan).getEarliestLoadingTime()
						.after((subOrdersSorted.get(scan + 1).getEarliestLoadingTime())))
					swap(subOrdersSorted, scan, scan + 1);
			}
		}
		//Searches the sorted ArrayList<SubOrder> from end to start, for all subOrders with highPriority and places them in front.
		for (int i = subOrdersSorted.size() - 1; i > 0; i--) {
			if (subOrdersSorted.get(i).isHighPriority() == true) {
				subOrdersSorted.add(0, subOrdersSorted.get(i));
				subOrdersSorted.remove(i + 1);
			}
		}
		return subOrdersSorted;
	}

	//Method used to swap objects in an ArrayList of any type. Used as part of the sortSubOrder and sortTrailerArrival sorting algorithms.
	private static <T> void swap(ArrayList<T> items, int index1, int index2)
	{
		T temp = items.get(index1);
		items.set(index1, items.get(index2));
		items.set(index2, temp);
	}

	/**
	 * A method used to set all subOrders initial earliestArrivalTime, based on the trailer they are carried by.
	 * @param trailer: Any trailer
	 * @author Jens Nyberg Porse 
	 */
	public static void setSubOrderEarliestLoadingTime(Trailer trailer)
	{

		//If the Trailer carries more then 1 subOrder, the first subOrders' earliestLoading time is set to the trailers timeOfArrival, the next subOrders' time is set to when the first is estimated to be done, and do on 
		if (trailer.getSubOrders().size() > 1) {
			Date time = trailer.getTimeOfArrival();
			ArrayList<SubOrder> trailerSubOrders = trailer.getSubOrders();
			for (SubOrder subOrder : trailerSubOrders) {
				subOrder.setEarliestLoadingTime(time);
				time = Service.getEndTime(time, subOrder.getEstimatedLoadingTime());
			}

			//If the Trailer only carries 1 subOrder, the subOrders earliestLoading time is set to the trailers timeOfArrival.
		} else {
			trailer.getSubOrders().get(0).setEarliestLoadingTime(trailer.getTimeOfArrival());

		}
	}

	/**
	 * A method that used to find the first avaliable LoadingBay available for the Suborder.
	 * @param productType: The productType the SubOrder is made of. Used to filter out loadingBays that cannot load this.
	 * @param earliestLoadingTime: The earliest time the SubOrder can be loaded unto a Truck.
	 * @return An Object of type LoadingBay, which has the shortest waitingtime, compared to when the SubOrder earliest can be loaded.
	 * @author Jens Nyberg Porse
	 */
	public static LoadingBay firstAvailableLoadingBay(ProductType productType,
			Date earliestLoadingTime)
	{

		//Creates a list of all LoadingBays in memory which can handle the productType in question
		ArrayList<LoadingBay> loadingBays = new ArrayList<LoadingBay>();

		for (int i = 0; i < Dao.getLoadingBays().size(); i++) {
			if (Dao.getLoadingBays().get(i).getProductType() == productType) {
				loadingBays.add(Dao.getLoadingBays().get(i));
			}
		}

		//Sets the first LoadingBay in the list as the baseline for comparison  
		LoadingBay earliestLoadingBay = loadingBays.get(0);
		Long shortestWaitTime = earliestLoadingBay.getNextFreeTime(earliestLoadingTime);

		//Loops through all LoadingBays, and asks for the loadingBays wait time in milliseconds, compared to the LoadinInfos earliest loading time
		for (int n = 0; n < loadingBays.size(); n++) {
			Long waitTime = (loadingBays.get(n).getNextFreeTime(earliestLoadingTime));
			//Compares the two waiting times
			if (waitTime <= shortestWaitTime) {
				earliestLoadingBay = loadingBays.get(n);
				shortestWaitTime = waitTime;
			}
		}
		//Before returning the earliest loadingBay, its nextAvailableTime is updated to the time it is ready to handle the LoadingInfo. 
		Date bayReadyAt = new Date(earliestLoadingTime.getTime() + shortestWaitTime);
		earliestLoadingBay.setNextAvailableTime(bayReadyAt);
		return earliestLoadingBay;
	}

	/**
	 * A method that creates the Schedule for when and where the SubOrders should be loaded.
	 * @param subOrders: A list of SubOrders that is either waiting to be loaded, or has yet to arrive at Danish Crown 
	 */
	public static void createLoadingBaySchedule(ArrayList<SubOrder> subOrders)
	{

		for (int i = 0; i < subOrders.size(); i++) {
			System.out.println("Starting a new Iteration:");
			System.out.println("Finding the optimal LoadingBay:");
			//Initiates all needed objects.
			SubOrder subOrder = subOrders.get(i);
			ProductType productType = subOrder.getProductType();
			Date earliestLoadingTime = subOrder.getEarliestLoadingTime();

			//Askes for the loadingbay with the shortest waiting time, compared to when the subOrder itself is ready to be loaded
			LoadingBay loadingBay = Service.firstAvailableLoadingBay(productType,
					earliestLoadingTime);

			System.out.println(subOrder + " is earliest ready to be loaded at: "
					+ Service.getDateToStringTime(earliestLoadingTime));
			System.out.println("Loadingbay " + loadingBay.getLoadingBayNumber()
					+ " is the first avaliable to handle " + subOrder);

			LoadingInfo loadingInfo = Service.createLoadingInfo(subOrder, loadingBay);
			System.out.println("New LoadingInfo created for " + subOrder);

			//Sets the scheduled time for when the LoadingInfo is to start
			loadingInfo.setTimeOfLoadingStart(loadingBay.getNextAvailableTime());
			System.out.println("Its Time of loading start is: "
					+ Service.getDateToStringTime(loadingInfo.getTimeOfLoadingStart()));
			loadingInfo.setTimeOfLoadingEnd(Service.getEndTime(loadingInfo.getTimeOfLoadingStart(),
					subOrder.getEstimatedLoadingTime()));
			System.out.println("Its Time of loading end is: "
					+ Service.getDateToStringTime(loadingInfo.getTimeOfLoadingEnd()));
			System.out.println();

			if (loadingInfo.getSubOrder().getTrailer().getTrailerState() == TrailerState.ARRIVED) {
				loadingInfo.setState(LoadingInfoState.READY_TO_LOAD);
			}

			//Sets the nextAvailableTime for the LoadingBay to the time when the SubOrder is loaded
			loadingBay.setNextAvailableTime(loadingInfo.getTimeOfLoadingEnd());
		}
	}

	/**
	 * A method that preps an ArrayList of SubOrders to be resorted in the LoadingBay Schedule
	 * @param productType: Used to create a local list of all loadingBays, whos SubOrders we are to resort
	 * @author Jens Nyberg Porse
	 */
	public static void refreshLoadingBays(ProductType productType)
	{
		//Creates a local list of all loadingBays from the Dao, that can handle the productType in question.
		ArrayList<LoadingBay> loadingBays = new ArrayList<LoadingBay>();
		for (LoadingBay loadingBay : Dao.getLoadingBays()) {
			if (loadingBay.getProductType() == productType)
				loadingBays.add(loadingBay);
		}

		//Prepares an ArrayList<SubOrder> adnd loops through the list of loadingBays
		ArrayList<SubOrder> subOrdersToSort = new ArrayList<SubOrder>();
		for (LoadingBay loadingBay : loadingBays) {
			for (LoadingInfo loadingInfo : loadingBay.getLoadingInfos()) {
				//Resets the loadingBays next available time to whenever the last subOrder was either started or finished
				if (loadingInfo.getState() == LoadingInfoState.LOADING
						|| loadingInfo.getState() == LoadingInfoState.FINISHED) {
					loadingBay.setNextAvailableTime(loadingInfo.getTimeOfLoadingEnd());
				}
				//Adds all subOrders to the ArrayList<SubOrder> who is either idle or waiting to be loaded.
				if (loadingInfo.getState() == LoadingInfoState.READY_TO_LOAD
						|| loadingInfo.getState() == LoadingInfoState.PENDING) {
					subOrdersToSort.add(loadingInfo.getSubOrder());
					//Removes the loadingInfos from the Dao and from the loadingBay, to copies.
					loadingBay.removeLoadingInfo(loadingInfo);
					Dao.removeLoadingInfo(loadingInfo);
				}
			}
		}
		System.out.println("sortSubOrder() began");
		//Sorts the Array of SubOrders
		subOrdersToSort = sortSubOrders(subOrdersToSort);
		System.out.println("createLoadingBayschedule() began");
		//Create a new loadingBay schedule
		Service.createLoadingBaySchedule(subOrdersToSort);
	}

	/**
	 * A small method used to calculate and return the date and time when a subOrder is estimated to be loaded.
	 * @param startTime: The time when the subOrder either begins, or is estimated to begin loading.
	 * @param loadingTime The estimated loadingTime for a subOrder.
	 * @return An estimated date for when the subOrder is loaded.
	 * @author Jens Nyberg Porse
	 */
	public static Date getEndTime(Date startTime, int loadingTime)
	{
		Long time = startTime.getTime();
		Long loadingTimeInMS = (long)(loadingTime * 60000);

		Date endTime = new Date(time + loadingTimeInMS);
		return endTime;
	}

	/**
	 * A small method only used in the programs GUI.
	 * @param date: Any date which time you wish to transform into a string
	 * @return A String containing the parameter date's time in the format "HH:MM"
	 * @author Jens Nyberg Porse 
	 */
	public static String getDateToStringTime(Date date)
	{
		String hours = String.format("%2d", date.getHours()).replace(' ', '0');
		String minutes = String.format("%2d", date.getMinutes()).replace(' ', '0');

		return hours + ":" + minutes;
	}

	/**
	 * A small method only used in the programs GUI.
	 * @param time: A string of the format "HH:MM", where HH = hours and MM = minutes
	 * @return A date object set to January 1, 2013 at HH:MM
	 * @author Jens Nyberg Porse
	 */
	public static Date getTimeStringToDate(String time)
	{
		int hours = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3, 5));
		Date date = new Date(113, 0, 1, hours, minutes);

		return date;
	}
}
