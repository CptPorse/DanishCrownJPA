package gui;

import model.Trailer;
import service.Service;
import service.StartUpService;
import dao.Dao;

//Author: Jens Nyberg Porse
public class SystemStartUp
{

	private static ExternalSystemView externalSystemView;
	private static TrailerView trailerView;
	private static LoadingBayView loadingBayView;

	public static void main(String[] args)
	{

		StartUpService.startUpData();
		Service.sortTrailerArrival();
		for (Trailer trailer : Dao.getTrailer()) {
			Service.setSubOrderEarliestLoadingTime(trailer);
		}
		StartUpService.sortSubOrdersDao();
		Service.createLoadingBaySchedule(Dao.getSubOrders());
		trailerView = new TrailerView();
		loadingBayView = new LoadingBayView();
		externalSystemView = new ExternalSystemView();
	}
}
