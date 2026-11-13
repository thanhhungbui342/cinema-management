package com.thanhhungbui342.cinema.repository.specification;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.thanhhungbui342.cinema.dto.request.ShowtimeFilterRequest;
import com.thanhhungbui342.cinema.entity.Showtime;
import com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ShowtimeSpecification {
    public static Specification<Showtime> filterShowtimes(ShowtimeFilterRequest filterRequest){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(); 
            
            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                root.fetch("movie", JoinType.LEFT);
                root.fetch("room", JoinType.LEFT);
                query.distinct(true);
            }

            if (filterRequest.fromDate() != null) {
                Instant startInstant = filterRequest.fromDate().atStartOfDay(ZoneOffset.UTC).toInstant();

                predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), startInstant));
            }

            if (filterRequest.toDate() != null) {
                Instant endInstant = filterRequest.toDate().atStartOfDay(ZoneOffset.UTC).toInstant();

                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), endInstant));
            }

            if (filterRequest.movieId() != null) {
                predicates.add(cb.equal(root.get("movie").get("id"), filterRequest.movieId()));
            }

            if (filterRequest.roomId() != null) {
                predicates.add(cb.equal(root.get("room").get("id"), filterRequest.roomId()));
            }

            predicates.add(cb.notEqual(root.get("status"), ShowtimeStatus.CANCELED));

            query.orderBy(cb.asc(root.get("startTime")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
