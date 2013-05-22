package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
@Entity
@Table(name = "Producttype")
public class ProductType
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "producttype_id")
	private long id;
	private String description;
	private double minuteToKiloRatio;

	@ManyToMany(mappedBy = "productTypes")
	private List<Trailer> trailers = new ArrayList<Trailer>();

	public ProductType()
	{

	}

	public ProductType(String description, double minuteToKiloRatio)
	{
		super();
		this.description = description;
		this.minuteToKiloRatio = minuteToKiloRatio;
	}

	public double getminuteToKiloRatio()
	{
		return minuteToKiloRatio;
	}

	public void setminuteToKiloRatio(double minuteToKiloRatio)
	{
		this.minuteToKiloRatio = minuteToKiloRatio;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Nullable
	public ArrayList<Trailer> getTrailer()
	{
		return new ArrayList<Trailer>(trailers);
	}

	public void addTrailer(Trailer trailer)
	{
		trailers.add(trailer);
	}

	public void removeTrailer(Trailer trailer)
	{
		trailers.remove(trailer);
	}

	@Override
	public String toString()
	{
		return "" + description;
	}

}
