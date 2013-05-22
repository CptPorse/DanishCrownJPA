package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.jdt.annotation.NonNullByDefault;

import service.Service;

//Author: Jens Nyberg Porse
@NonNullByDefault
@Entity
@Table(name = "Loadinginfo")
public class LoadingInfo
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "loadinginfo_id")
	private long id;
	@Enumerated(EnumType.STRING)
	private LoadingInfoState state;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date timeOfLoadingStart;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date timeOfLoadingEnd;

	@OneToOne(mappedBy = "loadingInfo")
	private SubOrder subOrder;

	@ManyToOne
	@JoinColumn(name = "loadingbay_id")
	private LoadingBay loadingBay;

	public LoadingInfo()
	{

	}

	public LoadingInfo(SubOrder subOrder, LoadingBay loadingBay)
	{
		super();
		this.subOrder = subOrder;
		this.loadingBay = loadingBay;
		state = LoadingInfoState.PENDING;
	}

	public Date getTimeOfLoadingStart()
	{
		return timeOfLoadingStart;
	}

	public void setTimeOfLoadingStart(Date timeOfLoadingStart)
	{
		this.timeOfLoadingStart = timeOfLoadingStart;
	}

	public Date getTimeOfLoadingEnd()
	{
		return timeOfLoadingEnd;
	}

	public void setTimeOfLoadingEnd(Date timeOfLoadingEnd)
	{
		this.timeOfLoadingEnd = timeOfLoadingEnd;
	}

	public SubOrder getSubOrder()
	{
		return subOrder;
	}

	public void setSubOrder(SubOrder subOrder)
	{
		this.subOrder = subOrder;
	}

	public LoadingBay getLoadingBay()
	{
		return loadingBay;
	}

	public void setLoadingBay(LoadingBay loadingBay)
	{
		this.loadingBay = loadingBay;
	}

	/**
	 * @return the state
	 */
	public LoadingInfoState getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(LoadingInfoState state)
	{
		this.state = state;
	}

	/**
	 * Method used when loading of a suborder is commenced.
	 * @param timeOfBegunLoading: Time of which loading begun.
	 * @author Søren Møller Nielsen
	 */
	public void beginLoading(Date timeOfBegunLoading)
	{
		setTimeOfLoadingStart(timeOfBegunLoading);
		setTimeOfLoadingEnd(Service.getEndTime(timeOfBegunLoading, subOrder.getEstimatedLoadingTime()));
		setState(LoadingInfoState.LOADING);
		loadingBay.setNextAvailableTime(timeOfLoadingEnd);
	}

	/**
	 * Method used then loading of a suborder is complete.
	 * @param timeOfEndedLoading: Time of which loading ended.
	 * @author Søren Møller Nielsen
	 */
	public void endLoading(Date timeOfEndedLoading)
	{
		setTimeOfLoadingEnd(timeOfEndedLoading);
		setState(LoadingInfoState.FINISHED);
		loadingBay.setNextAvailableTime(timeOfLoadingEnd);
		subOrder.setLoaded(true);

	}

	//Author: Christian Møller Pedersen
	@Override
	public String toString()
	{
		String line = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
		String bgColor = "";
		if (state == LoadingInfoState.FINISHED) {
			bgColor = "<body bgcolor='lime'>";
		} else if (state == LoadingInfoState.READY_TO_LOAD) {
			bgColor = "<body bgcolor='aqua'>";
		} else if (state == LoadingInfoState.LOADING) {
			bgColor = "<body bgcolor='yellow'>";
		}

		String hp = "";
		if (subOrder.isHighPriority() == true) {
			hp = "<h3 align='center'>HIGH PRIORITY</h3><br>";
		}
		return "<html>" + bgColor + hp + "<table><tr><td width='255'>" + subOrder + "</td><td width='255' align='right'>State: " + state + "</td></tr><br><tr><td>Estimated start: " + timeOfLoadingStart + " </td><td align='right'>Estimated loading time: " + subOrder.getEstimatedLoadingTime() + " min</td></tr></table>" + line;
	}
}
