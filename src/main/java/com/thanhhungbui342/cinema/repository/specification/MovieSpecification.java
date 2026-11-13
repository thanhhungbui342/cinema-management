package com.thanhhungbui342.cinema.repository.specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.thanhhungbui342.cinema.dto.request.MovieFilterRequest;
import com.thanhhungbui342.cinema.entity.Genre;
import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.MovieCast;
import com.thanhhungbui342.cinema.entity.Person;
import com.thanhhungbui342.cinema.entity.Showtime;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class MovieSpecification {

    public static Specification<Movie> filterMovies(MovieFilterRequest filterRequest){
        return (root, query, cb) -> {
            //Tránh trùng lặp khi JOIN
            query.distinct(true);
            Predicate predicate = cb.conjunction();

            //Lọc theo Status
            if(filterRequest.status() != null){
                predicate = cb.and(predicate, cb.equal(root.get("status"), filterRequest.status()));
            }

            //Lọc theo từ khóa(Tiltle or CastName)
            if(filterRequest.keyword() != null && !filterRequest.keyword().trim().isEmpty()){
                String pattern = "%" + filterRequest.keyword().trim().toLowerCase()+ "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), pattern);

                Join<Movie, MovieCast> castJoin = root.join("movieCast", JoinType.INNER);
                Join<MovieCast, Person> personJoin = castJoin.join("person", JoinType.INNER);

                Predicate personPredicate = cb.like(cb.lower(personJoin.get("name")), pattern);

                Predicate keywordPredicate = cb.or(titlePredicate, personPredicate);

                predicate = cb.and(predicate, keywordPredicate);
            }

            //Lọc theo thể loại
            if(filterRequest.genreIds() != null && !filterRequest.genreIds().isEmpty()){
                Join<Movie, Genre> genreJoin = root.join("genres", JoinType.INNER);
                predicate = cb.and(predicate, genreJoin.get("id").in(filterRequest.genreIds()));
            }

            //Lọc theo lịch chiếu(Rạp, ngày)
            if(filterRequest.cinemaId() != null || filterRequest.showDate() != null){
                Join<Movie, Showtime> showtimeJoin = root.join("showtimes", JoinType.INNER);

                //Lọc theo rạp
                if(filterRequest.cinemaId() != null){
                    predicate = cb.and(predicate, cb.equal(showtimeJoin.get("room").get("cimema").get("id"), filterRequest.cinemaId()));
                }

                //Lọc theo ngày
                if(filterRequest.showDate() != null){
                    LocalDateTime starOfDate = filterRequest.showDate().atStartOfDay();
                    LocalDateTime endOfDate = filterRequest.showDate().atTime(LocalTime.MAX);
                    predicate = cb.and(predicate, cb.between(showtimeJoin.get("startTime"), starOfDate, endOfDate)); 
                }
            }

            return predicate;
        };
    }
}
