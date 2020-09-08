package jpabook.start.mapping.join_strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
/**
 * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
 * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
 */
@DiscriminatorValue(value = "ALBUM")
@Table(name = "join_strategy_album")
@Entity(name = "JoinStrategyAlbum")
public class Album extends Item{

    private String artist;

    @Override
    public String toString() {
        return "Album{" +
                "artist='" + artist + '\'' +
                '}';
    }
}
