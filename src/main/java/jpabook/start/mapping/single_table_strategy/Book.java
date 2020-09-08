package jpabook.start.mapping.single_table_strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
/**
 * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
 * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
 */
@DiscriminatorValue(value = "BOOK")
@Table(name = "single_table_strategy_book")
@Entity(name = "SingleTableStrategyBook")
public class Book extends Item {

    private String author;
    private String isbn;

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
