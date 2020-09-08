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
@Table(name = "table_per_concrete_class_strategy_movie")
@Entity(name = "TablePerConcreteClassStrategyMovie")
public class Movie extends Item {

    private String director;
    private String actor;

    @Override
    public String toString() {
        return "Movie{" +
                "director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
