package model;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class Driver {

	private String name;
	private String phoneNumber;
	private String licensePlate;
	private Trailer trailer = null;

	public Driver(String name, String phoneNumber, String licensePlate) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.licensePlate = licensePlate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * Returns a list of Trailers carrying this productType.
	 */
	@Nullable
	public Trailer getTrailer() {
		return trailer;
	}

	/**
	 * Adds a Trailers to carry this productType.
	 */
	public void setTrailer(Trailer trailer) {
		this.trailer = trailer;
	}

	/**
	 * Removes a Trailers from carrying this productType.
	 */
	public void clearTrailer() {
		this.trailer = null;
	}

	@Override
	public String toString() {
		return "" + name;
	}

}
