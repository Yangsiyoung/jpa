package jpabook.start.value_type.embedded_type.advanced;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            Period employmentPeriod = new Period(LocalDate.now(), LocalDate.now().plusYears(3));

            Employee employee1 = new Employee(null, "employee1", employmentPeriod);

            entityManager.persist(employee1);

            entityManager.clear();

            Period attendancePeriod = new Period(LocalDate.now(), LocalDate.now().plusYears(4));
            Student student1 = new Student(null, "student1", attendancePeriod);

            entityManager.persist(student1);

            entityManager.clear();

            Employee findEmployee1 = entityManager.find(Employee.class, employee1.getId());
            System.out.println("findEmployee1 info is " + findEmployee1);

            Student findStudent1 = entityManager.find(Student.class, student1.getId());
            System.out.println("findStudent1 info is " + findStudent1);

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
