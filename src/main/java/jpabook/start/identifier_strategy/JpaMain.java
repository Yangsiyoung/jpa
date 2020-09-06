package jpabook.start.identifier_strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        // 엔티티 매니저 팩토리 생성(설정 파일 읽어오고, JPA 세팅하는 등 생성 비용이 크기 때문에 앱에서 딱 한번만 생성하고 공유해서 사용하자)
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");

        // 엔티티 매니저 생성
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 트랜잭션 획득
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{
            // 트랜잭션 시작
            entityTransaction.begin();

            // 로직 실행
            logic(entityManager);

            // 커밋
            entityTransaction.commit();
        }catch (Exception e) {
            System.out.println(e.toString());
            // Exception 났으니 트랜잭션 롤백
            entityTransaction.rollback();
        }finally {
            // 엔티티 매니저 종료
            entityManager.close();
        }
        // 엔티티 매니저 팩토리 종료(앱을 종료할 때)
        entityManagerFactory.close();



    }

    private static void logic(EntityManager entityManager) {
        String id = "id-1";
        String name = "양시영";
        Integer age = 28;

        Member member = new Member();
        //member.setId(id); // 기본 키 직접 할당
        member.setUserName(name);
        member.setAge(age);
        member.setRoleType(RoleType.USER);
        member.setCreatedDate(new Date());
        member.setLastModifiedDate(new Date());

        entityManager.persist(member);

        member.setAge(29);

        Member findMember = entityManager.find(Member.class, member.getId());
        System.out.println("findMember = " + findMember.getUserName() + ", age = " + findMember.getAge());

        List<Member> members = entityManager.createQuery("SELECT m from Member m", Member.class).getResultList();
        System.out.println("##############" + members.size());

//        entityManager.remove(member);
    }
}
