package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import dao.Dao;

@NonNullByDefault
@Entity
@Table(name = "Driver")
public class Driver
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "driver_id")
	private long id;
	private String name;
	private String phoneNumber;
	private String licensePlate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "trailer_id")
	private Trailer trailer;

	public Driver()
	{

	}

	public Driver(String name, String phoneNumber, String licensePlate)
	{
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.licensePlate = licensePlate;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		Dao.updateDatabase(this);

	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
		Dao.updateDatabase(this);
	}

	public String getLicensePlate()
	{
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate)
	{
		this.licensePlate = licensePlate;
		Dao.updateDatabase(this);
	}

	/**
	 * Returns a list of Trailers carrying this productType.
	 */
	@Nullable
	public Trailer getTrailer()
	{
		return trailer;
	}

	/**
	 * Adds a Trailers to carry this productType.
	 */
	public void setTrailer(Trailer trailer)
	{
		this.trailer = trailer;
		Dao.updateDatabase(this);
	}

	/**
	 * Removes a Trailers from carrying this productType.
	 */
	public void clearTrailer()
	{
		this.trailer = null;
		Dao.updateDatabase(this);
	}

	@Override
	public String toString()
	{
		return "" + name;
	}

}
