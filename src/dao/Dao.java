package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Driver;
import model.LoadingBay;
import model.LoadingInfo;
import model.Order;
import model.ProductType;
import model.SubOrder;
import model.Trailer;

public class Dao
{

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("dc");
	private static EntityManager em = emf.createEntityManager();

	/**
	 * Returns a list of all productTypes in the database.
	 */
	public static ArrayList<ProductType> getProductTypes()
	{
		List<ProductType> productTypes = new ArrayList<ProductType>();
		productTypes = em.createQuery("SELECT p FROM ProductType p", ProductType.class)
				.getResultList();

		return new ArrayList<ProductType>(productTypes);
	}

	/**
	 * Adds the productType to this in the database.
	 */
	public static void addProductType(ProductType productType)
	{
		em.getTransaction().begin();
		em.persist(productType);
		em.getTransaction().commit();
		System.out.println("Adding " + productType + " to the SQL database");
	}

	/**
	 * Removes the productType from this database.
	 */
	public static void removeProductType(ProductType productType)
	{
		em.getTransaction().begin();
		em.remove(productType);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of all drivers in the database.
	 */
	public static ArrayList<Driver> getDrivers()
	{
		List<Driver> drivers = new ArrayList<Driver>();
		drivers = em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();

		return new ArrayList<Driver>(drivers);
	}

	/**
	 * Adds the driver to this in the database.
	 */
	public static void addDriver(Driver driver)
	{
		em.getTransaction().begin();
		em.persist(driver);
		em.getTransaction().commit();
		System.out.println("Adding " + driver + " to the SQL database");
	}

	/**
	 * Removes the driver from this database.
	 */
	public static void removeDriver(Driver driver)
	{
		em.getTransaction().begin();
		em.remove(driver);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of all trailers in the database.
	 */
	public static ArrayList<Trailer> getTrailer()
	{
		List<Trailer> trailers = new ArrayList<Trailer>();
		trailers = em.createQuery("SELECT d FROM Driver d", Trailer.class).getResultList();

		return new ArrayList<Trailer>(trailers);
	}

	/**
	 * Adds the trailer to this database.
	 */
	public static void addTrailer(Trailer trailer)
	{
		em.getTransaction().begin();
		em.persist(trailer);
		em.getTransaction().commit();
		System.out.println("Adding " + trailer + " to the SQL database");
		;
	}

	/**
	 * Removes the trailer from this database.
	 */
	public static void removeTrailer(Trailer trailer)
	{
		em.getTransaction().begin();
		em.remove(trailer);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of sub-orders in the database.
	 */
	public static ArrayList<Order> getOrders()
	{
		List<Order> orders = new ArrayList<Order>();
		orders = em.createQuery("SELECT o FROM Order o", Order.class).getResultList();

		return new ArrayList<Order>(orders);
	}

	/**
	 * Adds the sub-order to this database.
	 */
	public static void addOrder(Order order)
	{
		em.getTransaction().begin();
		em.persist(order);
		em.getTransaction().commit();
		System.out.println("Adding " + order + " to the SQL database");
	}

	/**
	 * Removes the sub-order from this database.
	 */
	public static void removeOrder(Order order)
	{
		em.getTransaction().begin();
		em.remove(order);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of sub-orders in the database.
	 */
	public static ArrayList<SubOrder> getSubOrders()
	{
		List<SubOrder> subOrders = new ArrayList<SubOrder>();
		subOrders = em.createQuery("SELECT s FROM SubOrder s", SubOrder.class).getResultList();

		return new ArrayList<SubOrder>(subOrders);
	}

	/**
	 * Adds the sub-order to this database.
	 */
	public static void addSubOrder(SubOrder subOrder)
	{
		em.getTransaction().begin();
		em.persist(subOrder);
		em.getTransaction().commit();
		System.out.println("Adding " + subOrder + " to the SQL database");
	}

	/**
	 * Removes the sub-order from this database.
	 */
	public static void removeSubOrder(SubOrder subOrder)
	{
		em.getTransaction().begin();
		em.remove(subOrder);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of LoadingInfo in the database.
	 */
	public static ArrayList<LoadingInfo> getLoadingInfos()
	{
		List<LoadingInfo> loadingInfos = new ArrayList<LoadingInfo>();
		loadingInfos = em.createQuery("SELECT l FROM LoadingInfo l", LoadingInfo.class)
				.getResultList();

		return new ArrayList<LoadingInfo>(loadingInfos);
	}

	/**
	 * Adds the LoadingInfo to this database.
	 */
	public static void addLoadingInfo(LoadingInfo loadingInfo)
	{
		em.getTransaction().begin();
		em.persist(loadingInfo);
		em.getTransaction().commit();
		System.out.println("Adding " + loadingInfo + " to the SQL database");
	}

	/**
	 * Removes the LoadingInfo from this database.
	 */
	public static void removeLoadingInfo(LoadingInfo loadingInfo)
	{
		em.getTransaction().begin();
		em.remove(loadingInfo);
		em.getTransaction().commit();
	}

	/**
	 * Returns a list of LoadingBay in the database.
	 */
	public static ArrayList<LoadingBay> getLoadingBays()
	{
		List<LoadingBay> loadingBays = new ArrayList<LoadingBay>();
		loadingBays = em.createQuery("SELECT l FROM LoadingBay l", LoadingBay.class)
				.getResultList();

		return new ArrayList<LoadingBay>(loadingBays);
	}

	/**
	 * Adds the LoadingInfo to this database.
	 */
	public static void addLoadingBay(LoadingBay loadingBay)
	{
		em.getTransaction().begin();
		em.persist(loadingBay);
		em.getTransaction().commit();
		System.out.println("Adding " + loadingBay + " to the SQL database");
	}

	/**
	 * Removes the LoadingBay from this database.
	 */
	public static void removeLoadingBay(LoadingBay loadingBay)
	{
		em.getTransaction().begin();
		em.remove(loadingBay);
		em.getTransaction().commit();
	}

}
