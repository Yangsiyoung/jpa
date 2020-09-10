package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

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

            Parent parent1 = new Parent();
            parent1.setId("parentId1");
            parent1.setName("parent1");

            entityManager.persist(parent1);

            entityManager.flush();

            /**
             * entityManager.clear(); 해버리면 밑에서 parent1 Entity 저장 안된줄알고 Child persist 시 Parent Entity DB 에 넣으려고해서
             * 이미 위헤서 동일한 식별자를 가지고 있는 Parent Table 의 정보와 충돌남
             *
             * 복합 키 - 비식별자 매핑에서는 자식 테이블에서 부모 테이블의 기본 키를 외래 키로 사용해서 문제가 없었지만
             * 복합 키 - 식별자 매핑에서는 자식 테이블에서 부모 테이블의 기본 키를 기본 키 + 외래 키 로 사용하기 때문에
             * 복합 키(부모 테이블로 부터 내려온 것)가 없어보이면 강제로 만들려고 하는 것 같다
             *
             * 보통 서비스에서는 자식 테이블의 값을 넣을 때 부모 테이블에 값이 존재하고,
             * 존재하는 부모 테이블의 값을 가져와서 자식 Entity 에 정보를 매핑하고
             * persist 할 텐데, 무조건 부모 테이블까지 INSERT 하는 줄 알고 복합 키 - 식별자 매핑은
             * 실 서비스에서 사용하지 못할 것 같아 걱정했는데
             *
             * 결론은 Persistence Context 에 부모 Entity 의 정보가 있으면 새로 만들지 않는 것 같다.
             *
             * 따라서 일반적으로 서비스에 자식 Entity 정보를 삽입할 때,
             * 부모 Entity 를 조회해서 알맞은 데이터를 자식 Entity 에 설정하여
             * 값을 저장할테니, 부모 Entity 를 조회해오는 과정에서 Persistence Context 에서 관리 되니까
             * 걱정 안해도 될 것 같다.
             */
            // entityManager.clear();

            ChildId childId = new ChildId();
            childId.setParentId(parent1.getId());
            childId.setChildId("childId1");

            Child child1 = new Child();
            child1.setChildId(childId);
            child1.setName("child1");
            child1.setParent(parent1);

            entityManager.persist(child1);

            entityManager.flush();

            /**
             * entityManager.clear(); 해버리면 밑에서 child1 Entity 저장 안된줄알고 GrandChild persist 시 Child Entity DB 에 넣으려하고
             * 또 위에서 parent persist 후 entityManager.clear(); 해버렸기 때문에 Parent Entity 도 DB 에 넣으려하고
             * 이미 위헤서 동일한 식별자를 가지고 있는 Parent, Child Table 의 정보와 충돌남
             */
            // entityManager.clear();

            GrandChildId grandChildId = new GrandChildId();
            grandChildId.setGrandChildParentId(childId);
            grandChildId.setGrandChildId("grandChildId1");

            GrandChild grandChild1 = new GrandChild();
            grandChild1.setGrandChildId(grandChildId);
            grandChild1.setChild(child1);
            grandChild1.setName("grandChild1");

            entityManager.persist(grandChild1);

            entityManager.flush();
            entityManager.clear();

            Parent findParent1 = entityManager.find(Parent.class, parent1.getId());
            System.out.println("findParent1 info is " + findParent1);

            Child findChild1 = entityManager.find(Child.class, childId);
            System.out.println("findChild1 info is "+ findChild1);

            GrandChild findGrandChild1 = entityManager.find(GrandChild.class, grandChildId);
            System.out.println("findGrandChild1 info is " + findGrandChild1);

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
