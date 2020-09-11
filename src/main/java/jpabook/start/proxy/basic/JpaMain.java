package jpabook.start.proxy.basic;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.HibernateIterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Iterator;

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

            // entityManager.flush(); @GeneratedValue(strategy = GenerationType.IDENTITY)
            entityManager.clear();

            Member member1 = new Member();
            member1.setUserName("member1");
            member1.setTeam(team1);

            entityManager.persist(member1);

            // entityManager.flush(); @GeneratedValue(strategy = GenerationType.IDENTITY)
            entityManager.clear();

            /**
             * Entity 조회(즉시 로딩) : entityManager.find();
             * Entity Proxy 객체 조회(지연 로딩) : entityManager.getReference();
             *
             * Proxy Class 의 경우 실제 Entity Class 를 상속받아 만들어지기 때문에
             * Entity 와 겉모습이 같다.
             *
             * 하지만 entityManager.find() 를 통해 조회할 시 해당 Entity 를 즉시 조회하도록
             * Persistence Context 에 요청하고 Persistence Context 에 없으면 DB 를 조회하는데,
             *
             * entityManager.getReference() 를 통해 조회할 시 해당 Entity 를 상속 받은 Proxy 객체가 리턴되고,
             * Proxy 객체는 마치 해당 Entity 처럼 다뤄지는데 빈 값이 들어있는 껍데기에 불과하다.
             *
             * 실제로 referenceMember1.getUserName() 처럼 사용될 때 Persistence Context 에 실제 엔티티 정보를 가져오도록
             * 요청을 한다.(DB 에 SELECT 쿼리가 나가며 Entity 정보를 가져와서 프록시 객체에 세팅한다. => 조회해와도 Type 이 Proxy Class 이다.)
             *
             * 만약에 entityManager.getReference() 를 사용해서 조회하는 Entity 가 Persistence Context 에 존재하는 Entity 라면
             * 바로 해당 Entity 를 리턴하기 때문에 Type 이 Entity Class 이다.
             * (Proxy Class 를 사용하는 이유는 불필요한 쿼리를 발생시키지 않고 정말 필요할 때 쿼리를 발생시키기 위함인데
             *  이미 Persistence Context 에 있는 Entity 일 경우에는 쿼리를 사용하지 않아도 가져올 수 있기 때문이라고 생각한다.)
             *
             * Persistence Context 해당 Entity 정보를 조회 요청을 하고, Persistence Context 해당 Entity 정보가 없으면 빈 껍데기인 Proxy Class 가
             * 실제로 사용될때에 DB 에 SELECT 쿼리를 보내서 Persistence Context 에 해당 Entity 의 정보를 가져와 세팅하거나
             * Persistence Context 에 해당 Entity 정보를 조회 요청을 하고, Persistence Context 해당 Entity 정보가 있으면,
             * Entity 를 그대로 받아서 Proxy Class 가 아닌 해당 Entity 를 리턴하는 등 Persistence Context 에게 의존하고 있다.
             *
             * 따라서 이 Proxy Class 는 준영속 상태가 되면 사용이 불가능 하다.
             *
             *
             *
             *
             */
            Member referenceMember1 = entityManager.getReference(Member.class, member1.getId());
            System.out.println("SELECT Query is NOT Executed");

            // class jpabook.start.proxy.basic.Member$HibernateProxy
            System.out.println("referenceMember1 type is " + referenceMember1.getClass());

            // Proxy Entity 는 식별자를 보관하고 있으므로, referenceMember1.getId() 호출시에는 SELECT 쿼리 발생 X
            System.out.println("referenceMember1 id is " + referenceMember1.getId());

            // 실제로 referenceMember1 Proxy Entity 가 사용될 때 FIND 해 옴
            System.out.println(referenceMember1);
            System.out.println(referenceMember1.getUserName());
            System.out.println(referenceMember1.getTeam());

            // 위에서 동일한 식별자를 가진 Entity 를 리턴하지않고 Entity 의 Proxy Class 를 리턴 함.
            Member referenceMember2 = entityManager.getReference(Member.class, member1.getId());
            System.out.println("SELECT Query is NOT Executed");

            // class jpabook.start.proxy.basic.Member$HibernateProxy
            System.out.println("referenceMember2 type is " + referenceMember2.getClass());

            /**
             * 실제로 referenceMember2 Proxy Entity 가 사용될 때 FIND 해 오는데 위에서
             * Persistence Context 에 등록된 식별자의 Proxy 이므로 쿼리 발생 X
             */
            System.out.println(referenceMember2);
            System.out.println(referenceMember2.getUserName());
            System.out.println(referenceMember2.getTeam());
            /**
             * entityManager.clear()
             * 하지않으면 밑에서 entityManager.find() 하면
             * 위에서 Persistence Context 에 등록된 Proxy Class 를 리턴받음.
             */
            entityManager.clear();

            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("SELECT Query is Executed");

            // jpabook.start.proxy.basic.Member
            System.out.println("findMember1 type is " + findMember1.getClass());
            System.out.println(findMember1);
            System.out.println(findMember1.getUserName());
            System.out.println(findMember1.getTeam());

            Member referenceMember3 = entityManager.getReference(Member.class, member1.getId());
            System.out.println("SELECT Query is NOT Executed");

            // class jpabook.start.proxy.basic.Member$HibernateProxy$0p1z1OvY
            System.out.println("referenceMember2 type is " + referenceMember3.getClass());

            // 실제로 referenceMember2 Proxy Entity 가 사용될 때 FIND 해 옴
            System.out.println(referenceMember3);
            System.out.println(referenceMember3.getUserName());
            System.out.println(referenceMember3.getTeam());

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
