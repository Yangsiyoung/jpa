package jpabook.start.relational_mapping.one_way.one_to_many;

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

            entityTransaction.begin();;

            Member member1 = new Member();
            member1.setUserName("member1");

            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setUserName("member2");

            entityManager.persist(member2);

            Team team1 = new Team();
            team1.setName("team1");
            team1.getMembers().add(member1);
            team1.getMembers().add(member2);

            entityManager.persist(team1);
            System.out.println("team1 is " + team1);

            /**
             * entityManager.persist(team1); 할 때 Insert Query 는 바로 나가지만
             * team Insert 이 후 Member 에 해당하는 Table 의 team_id 를 update 하는 쿼리는
             * entityTransaction.commit(); 실행시에 나가기 때문에
             * 미리 entityManager.flush(); 를 해줘야 함
             */
            entityManager.flush();

            // DB 에 잘들어갔나 find 하기 위해 Persistence Context Cache 및 Snapshot 을 clear 함
            entityManager.clear();

            Team findTeam1 = entityManager.find(Team.class, team1.getId());
            System.out.println("findTeam1 is " + findTeam1.toString());

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
