package org.example.core.repositories;

import org.example.core.entities.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.JoinType;
import java.util.List;

@Component
public class HotelSpecification {

    public static Specification<Hotel> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase());
    }

    public static Specification<Hotel> inCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.join("address").get("city")), city.toLowerCase());
    }

    public static Specification<Hotel> inCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.join("address").get("country")), country.toLowerCase()
                );
    }

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("amenities", JoinType.INNER).get("name").in(amenities);
        };
    }

    private HotelSpecification() {
    }
}