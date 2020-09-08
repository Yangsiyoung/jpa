package jpabook.start.mapping.table_per_concrete_class_strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "table_per_concrete_class_strategy_book")
@Entity(name = "TablePerConcreteClassStrategyBook")
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
