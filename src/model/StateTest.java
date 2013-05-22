package model;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dateutil.DU;

// Author Jens Nyberg Porser
public class StateTest
{
	//All objects needed for the simplest test environment
	Trailer testingTrailer;
	LoadingInfo testingLoadingInfo;
	ProductType productType;
	LoadingBay loadingBay;
	Order order1;
	SubOrder subOrder1, subOrder2;
	LoadingInfo testLoadingInfo1, testLoadingInfo2;
	Date date1, date2;

	@Before
	public void setUp() throws Exception
	{
		//Initializing the objects and linking them as the program would
		date1 = new Date(113, 0, 1, 9, 0);
		date2 = new Date(113, 0, 1, 10, 0);
		testingTrailer = new Trailer("1", 25000.0, date1);
		productType = new ProductType("Christmas Trees", 0.0031);
		testingTrailer.addProductType(productType);
		loadingBay = new LoadingBay(1, productType);
		order1 = new Order(1, DU.createDate("2013-01-01"));
		subOrder1 = new SubOrder(8000, testingTrailer, productType);
		subOrder2 = new SubOrder(10000, testingTrailer, productType);
		testLoadingInfo1 = new LoadingInfo(subOrder1, loadingBay);
		testLoadingInfo2 = new LoadingInfo(subOrder2, loadingBay);
		subOrder1.setLoadingInfo(testLoadingInfo1);
		subOrder2.setLoadingInfo(testLoadingInfo2);
	}

	@Test
	public void testTrailerStateChanges()
	{
		//Testing a newly created trailer
		assertTrue(TrailerState.IDLE == testingTrailer.getTrailerState());
		//Adding the suborders to the trailer
		order1.addSubOrder(subOrder1);
		order1.addSubOrder(subOrder2);
		testingTrailer.addSubOrder(subOrder1);
		testingTrailer.addSubOrder(subOrder2);
		assertTrue(TrailerState.ENROUTE == testingTrailer.getTrailerState());
		//Register the trailers arrival at Danish Crown
		testingTrailer.registerArrival();
		assertTrue(TrailerState.ARRIVED == testingTrailer.getTrailerState());
		//Begin loading the 1. suborder unto the trailer
		testingTrailer.beginLoading(testLoadingInfo1);
		assertTrue(TrailerState.BEING_LOADED == testingTrailer.getTrailerState());
		//End loading the trailer
		testingTrailer.endLoading(testLoadingInfo1);
		assertTrue(TrailerState.ARRIVED == testingTrailer.getTrailerState());
		//Begin loading the 2. suborder unto the trailer
		testingTrailer.beginLoading(testLoadingInfo2);
		assertTrue(TrailerState.BEING_LOADED == testingTrailer.getTrailerState());
		//End loading the trailer
		testingTrailer.endLoading(testLoadingInfo2);
		assertTrue(TrailerState.LOADED == testingTrailer.getTrailerState());
		//Register the departure of the trailer
		testingTrailer.registerDeparture();
		assertTrue(TrailerState.DEPARTED == testingTrailer.getTrailerState());

	}

	@Test
	public void testLoadingInfoStateChanges()
	{
		//Testing two newly created loadingInfos
		assertTrue(LoadingInfoState.PENDING == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.PENDING == testLoadingInfo2.getState());
		//Register the arrival of the two loadingInfo's suborder
		order1.addSubOrder(subOrder1);
		order1.addSubOrder(subOrder2);
		testingTrailer.addSubOrder(subOrder1);
		testingTrailer.addSubOrder(subOrder2);
		testingTrailer.registerArrival();
		assertTrue(LoadingInfoState.READY_TO_LOAD == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.PENDING == testLoadingInfo2.getState());
		//Begin loading the first loadingInfos suborder
		testLoadingInfo1.beginLoading(date1);
		assertTrue(LoadingInfoState.LOADING == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.PENDING == testLoadingInfo2.getState());
		//End loading the first loadingInfos suborder
		testLoadingInfo1.endLoading(date2);
		assertTrue(LoadingInfoState.FINISHED == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.PENDING == testLoadingInfo2.getState());
		//Begin loading the second loadingInfos suborder
		testLoadingInfo2.beginLoading(date1);
		assertTrue(LoadingInfoState.FINISHED == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.LOADING == testLoadingInfo2.getState());
		//End loading the first loadingInfos suborder
		testLoadingInfo2.endLoading(date2);
		assertTrue(LoadingInfoState.FINISHED == testLoadingInfo1.getState());
		assertTrue(LoadingInfoState.FINISHED == testLoadingInfo2.getState());

	}
}
