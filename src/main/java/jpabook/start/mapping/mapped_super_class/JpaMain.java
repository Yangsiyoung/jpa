package jpabook.start.mapping.mapped_super_class;

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

            Member member1 = new Member();
            member1.setName("member1");
            member1.setEmail("ysjleader@gmail.com");

            entityManager.persist(member1);

            Seller seller1 = new Seller();
            seller1.setName("seller1");
            seller1.setShopName("seller1 shop");

            entityManager.persist(seller1);

            entityManager.clear();

            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 info is " + findMember1);

            Seller findSeller1 = entityManager.find(Seller.class, seller1.getId());
            System.out.println("findSeller1 info is " + findSeller1);

            entityTransaction.commit();

        }catch (Exception e) {
            entityTransaction.rollback();
            System.out.println(e.toString());
        }finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
