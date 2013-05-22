package model;

//Author: Soren Moller Nielsen
public enum TrailerState
{
	/*
	 * IDLE:			When a trailer haven't got a suborder yet. 
	 * ENROUTE:			Whene a trailer got a trailer, but haven't arrived on the factory yet. 
	 * ARRIVED:			When the trailer is ready for being loaded. 
	 * BEING_LOADED:	When a trailer is in the process of being loaded. 
	 * LOADED:			Ready to departure, just need to have its weight checked.
	 * DEPARTED:		The trailer has left the factory.
	 */

	IDLE, ENROUTE, ARRIVED, BEING_LOADED, LOADED, DEPARTED;
}
