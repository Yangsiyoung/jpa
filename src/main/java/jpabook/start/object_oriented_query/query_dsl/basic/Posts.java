package jpabook.start.object_oriented_query.query_dsl.basic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "query_dsl_posts")
@Entity(name = "QueryDSLPosts")
public class Posts {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postId;

    private String title;

    private String contents;

    private Integer memberId;

    @Override
    public String toString() {
        return "Posts{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", memberId=" + memberId +
                '}';
    }
}
