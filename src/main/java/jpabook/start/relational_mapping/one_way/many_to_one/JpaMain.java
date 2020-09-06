package jpabook.start.relational_mapping.one_way.many_to_one;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            // Team 생성
            Team team1 = new Team();
            team1.setName("team1");

            // Team DB에 저장 + Persistence Context 로 관리 시작
            entityManager.persist(team1);

            Team team2 = new Team();
            team2.setName("team2");

            entityManager.persist(team2);

            Member member1 = new Member();
            member1.setUserName("member1");
            member1.setTeam(team1);

            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setUserName("member2");
            member2.setTeam(team1);

            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUserName("member3");

            entityManager.persist(member3);

            System.out.println("member1 info is " + member1);
            System.out.println("member1.getTeam() is  " + member1.getTeam());

            System.out.println("member2 info is " + member2);
            System.out.println("member2.getTeam() is  " + member2.getTeam());

            System.out.println("member3 info is " + member3);
            System.out.println("member3.getTeam() is  " + member3.getTeam());

            /**
             * jpql로 조회하기(jpql 실행 시 쿼리는 자동으로 flush 됨)
             * Table 명 대신 Entity Name
             */
            //String jpql = "SELECT m FROM ManyToOneMember m JOIN m.team t WHERE m.team = t AND t.name = :teamName";
            String jpql = "SELECT m FROM ManyToOneMember m, ManyToOneTeam t WHERE m.team = t AND t.name = :teamName";
            List<Member> members = entityManager.createQuery(jpql, Member.class).setParameter("teamName", "team1").getResultList();
            System.out.println("jpql team1 Members info is " + members);

            /**
             * DB 로 Query 보내기, 현재는 GenerationType.IDENTITY 를 사용하므로 persist 시에
             * 바로 쿼리가 실행되므로, 지금까지는 쿼리가 flush 된 상태이기 때문에, entityManager.flush() 사용 안했음
             */

            // Persistence Context Cache 지우기
            entityManager.clear();

            // entityManager.clear() 가 실행되어 Persistence Context Cache 지워졌으므로 다시 조회해 옴 + Persistence Context 에서 관리 시작
            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 info is " + findMember1);

            // Persistence Contetxt 내 Cache 의 Snapshot 과 비교해서 변경된 부분이 있으면 자동으로 UPDATE 쿼리 생성해 줌(쿼리 실행은 commit() OR entityManager.flush() 실행 시점)
            findMember1.setTeam(team2);
            // entityManager.flush();
            System.out.println("findMember1 change team " + findMember1);

            entityTransaction.commit();

        }catch (Exception e) {
            entityTransaction.rollback();
            // sout + tab
            System.out.println(e.toString());
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
