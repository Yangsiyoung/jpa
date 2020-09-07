package jpabook.start.relational_mapping.one_way.one_to_many;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @ManyToOne - @OneToMany 양방향 매핑을 공부해보았다.
 * 아래 코드에서 연관관계 편의 메소드등을 작성해보지는 않았다.
 *
 * 이렇게 양방향 연관관계에 대해서 간단하게만 작성해본 이유는 아래와 같다.
 *
 * 양방향 연관관계를 맺을 떄 주의점이 많고, 실수할 가능성도 많기때문에
 * 꼭 필요할 때만 사용하고, 사용할 때엔 조금 더 공부를 해서 그 당시의 상황에 맞게
 * 사용하기 위함이다.
 */
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

            Member member1 = new Member();
            member1.setUserName("member1");
            member1.setTeam(team1);

            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setUserName("member2");
            member2.setTeam(team1);

            entityManager.persist(member2);


            /**
             * DB 로 Query 보내기, 현재는 GenerationType.IDENTITY 를 사용하므로 persist 시에
             * 바로 쿼리가 실행되므로, 지금까지는 쿼리가 flush 된 상태이기 때문에, entityManager.flush() 사용 안했음
             */

            /**
             * Persistence Context 비우기(Cache, Snapshot 날아감)
             * clear 를 해주지 않으면 아래 find 에서 아무런 동작이 일어나지 않음,
             * Team Entity 에 @OneToMany(mappedBy="") 를 설정해두었더라도,
             * 현재 team1 의 경우 위에서 persist 하는 과정에서 1차 캐시에 존재하기 때문에
             * find 를 하더라도 1차 캐시에서 가져오기 때문
             *
             * 따라서 clear 를 해줘야 제대로 들고온다.
             */
            entityManager.clear();
            Team findTeam1 = entityManager.find(Team.class, team1.getId());

            findTeam1.getMembers().stream().forEach((member)-> System.out.println(member.getUserName()));



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
