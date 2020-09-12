package jpabook.start.proxy.lazy_loading;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            Team team1 = new Team();
            team1.setName("team1");

            entityManager.persist(team1);

            entityManager.clear();;

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team1);

            entityManager.persist(member1);

            entityManager.clear();

            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("SELECT Query is Executed with out JOIN");

            System.out.println("findMember1 name is " + findMember1.getName());
            System.out.println("findMember1 team info is " + findMember1.getTeam());
            System.out.println("SELECT Query is Executed for Find findMember1s' Team");

            entityTransaction.commit();

        } catch (Exception e) {
            entityTransaction.rollback();
            System.out.println(e.toString());
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
