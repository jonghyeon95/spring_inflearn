package jpabook.jpashop.Domain.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@SuperBuilder @NoArgsConstructor @AllArgsConstructor
@Entity
@Data
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;

}

