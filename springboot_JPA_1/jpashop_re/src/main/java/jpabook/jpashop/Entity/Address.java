package jpabook.jpashop.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
