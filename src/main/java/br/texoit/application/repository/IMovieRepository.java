package br.texoit.application.repository;

import br.texoit.application.model.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMovieRepository extends JpaRepository<Movies, Long> {
    Optional<Movies> findByYearAndTitleAndStudiosAndProducerAndWinner(String year, String title, String studios,
                                                                      String producer, String winner);
}
