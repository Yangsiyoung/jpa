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
@Table(name = "table_per_concrete_class_strategy_album")
@Entity(name = "TablePerConcreteClassStrategyAlbum")
public class Album extends Item {

    private String artist;

    @Override
    public String toString() {
        return "Album{" +
                "artist='" + artist + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
