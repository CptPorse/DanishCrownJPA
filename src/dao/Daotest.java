package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Daotest
{
	public static void main(String[] args)
	{
		System.out.println("1");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("dc");
		System.out.println("2");
		EntityManager em = emf.createEntityManager();
		System.out.println("3");
//		Trailer t1 = new Trailer("1", 12, null);

//		em.getTransaction().begin();
//		em.persist(t1);
//		em.getTransaction().commit();

		em.close();
		emf.close();
	}
}
