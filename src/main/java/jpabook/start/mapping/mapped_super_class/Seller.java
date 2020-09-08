package jpabook.start.mapping.mapped_super_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "mapped_super_class_seller")
@Entity(name = "MappedSuperClassSeller")
public class Seller extends BaseEntity {

    @Column(name = "shop_name")
    private String shopName;

    @Override
    public String toString() {
        return "Seller{" +
                "shopName='" + shopName + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
