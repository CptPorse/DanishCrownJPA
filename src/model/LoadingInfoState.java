package model;

//Author: Christian Møller Pedersen
/*
 * PENDING: 		Trailer is on its way to DC.
 * READY_TO_LOAD: 	Trailer has arrived at DC and is ready to begin loading.
 * LOADING: 		LoadingInfo is currently being loaded.
 * FINISHED: 		LoadingInfo is loaded and finished.
 */
public enum LoadingInfoState
{
	PENDING, READY_TO_LOAD, LOADING, FINISHED;
}
