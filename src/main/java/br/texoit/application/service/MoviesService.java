package br.texoit.application.service;

import br.texoit.application.dto.request.MovieDTO;
import br.texoit.application.dto.response.ProducerInfoDTO;
import br.texoit.application.dto.response.ProducersDTO;
import br.texoit.application.dto.response.ServiceResponse;
import br.texoit.application.enuns.MessageSystem;
import br.texoit.application.exception.ResourceNotFoundException;
import br.texoit.application.model.Movies;
import br.texoit.application.repository.IMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MoviesService {

    private final IMovieRepository movieRepository;
    private final FilesService filesService;

    @PostConstruct
    public void init() {
        log.info("Início da Aplicação");
        log.info("Início da importação");
        try {
            MultipartFile file = getFileMoviesCSV();
            List<MovieDTO> movies = filesService.fileToEntity(file);
            saveMovies(movies);
            log.info("Registros importados: {}", movieRepository.count());
        } catch (IOException e) {
            log.error("Erro durante a importação: {}", e.getMessage());
        }
        log.info("Final da importação");
    }

    private static MultipartFile getFileMoviesCSV() throws IOException {
        File file = new File("src/test/resources/test-movies.csv");
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file", file.getName(), "text/csv", input);
    }

    public ServiceResponse saveMovies(List<MovieDTO> movies) {
        try {

            movies.forEach(m -> {
                Optional<Movies> movie = movieRepository.findByYearAndTitleAndStudiosAndProducerAndWinner(
                        m.getYear(), m.getTitle(), m.getStudios(), m.getProducer(), m.getWinner());

                if (!movie.isPresent()) {
                    movieRepository.save(Movies.builder()
                            .year(m.getYear())
                            .title(m.getTitle())
                            .studios(m.getStudios())
                            .producer(m.getProducer())
                            .winner(m.getWinner())
                            .date(LocalDateTime.now()).build());
                }
            });

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_REGISTER_MOVIES.value())
                    .data(null).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_FUNCTIONAL.value() + " - "+ ex.getMessage();
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    public ServiceResponse getMovies(long id){
        try {
            Optional<Movies> movie = movieRepository.findById(id);

            if (!movie.isPresent())
                return ServiceResponse.builder()
                        .status(true)
                        .message(MessageSystem.ERROR_EMPTY_RESULTS.value())
                        .data(null).build();

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_RETRIEVE_MOVIES.value())
                    .data(movie.get()).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_FUNCTIONAL.value() + " - "+ ex.getMessage();
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    public ServiceResponse refreshMovie(long id, MovieDTO info){
        try {
            Optional<Movies> movieOptional = movieRepository.findById(id);

            if (!movieOptional.isPresent())
                throw new ResourceNotFoundException(MessageSystem.ERROR_EMPTY_RESULTS.value());

            Movies movie = movieOptional.get();

            if (info.getYear() != null && !info.getYear().isEmpty())
                movie.setYear(info.getYear());
            if (info.getTitle() != null && !info.getTitle().isEmpty())
                movie.setTitle(info.getTitle());
            if (info.getStudios() != null && !info.getStudios().isEmpty())
                movie.setStudios(info.getStudios());
            if (info.getProducer() != null && !info.getProducer().isEmpty())
                movie.setProducer(info.getProducer());

            movie.setWinner(info.getWinner());

            movieRepository.save(movie);

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_UPDATED_MOVIES.value())
                    .data(null).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_FUNCTIONAL.value() + " - "+ ex.getMessage();
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    public ServiceResponse deleteMovie(long id){
        try {
            Optional<Movies> movie = movieRepository.findById(id);

            if (!movie.isPresent())
                throw new ResourceNotFoundException(MessageSystem.ERROR_EMPTY_RESULTS.value());

            movieRepository.delete(movie.get());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_DELETED_MOVIES.value())
                    .data(null).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_FUNCTIONAL.value() + " - "+ ex.getMessage();
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    public ServiceResponse prizeRange(){
        try {
            List<Movies> movies = movieRepository.findAll();
            List<ProducerInfoDTO> producers = new ArrayList<>();

            if (movies.isEmpty())
                throw new ResourceNotFoundException(MessageSystem.ERROR_EMPTY_RESULTS.value());

            movies.forEach(movie ->{
                int prevWin = Integer.parseInt(movie.getYear());
                int folwWin = movie.getWinner().equals("yes") ? Integer.parseInt(movie.getYear()) : -1;

                producers.add(ProducerInfoDTO.builder()
                                .producer(movie.getProducer())
                                .interval(folwWin - prevWin)
                                .previousWin(prevWin)
                                .followingWin(folwWin).build());
            });

            ProducerInfoDTO maxInterval = producers.stream()
                    .max(Comparator.comparingInt(p -> p.getInterval()))
                    .orElse(null);

            ProducerInfoDTO minInterval = producers.stream()
                    .filter(p -> p.getFollowingWin() != -1).min(Comparator.comparingInt(p -> p.getInterval()))
                    .orElse(null);

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS.value())
                    .data(ProducersDTO.builder()
                            .min(Arrays.asList(minInterval))
                            .max(Arrays.asList(maxInterval)).build()).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_FUNCTIONAL.value() + " - "+ ex.getMessage();
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }
}
