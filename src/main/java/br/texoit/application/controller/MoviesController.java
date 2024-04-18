package br.texoit.application.controller;

import br.texoit.application.dto.request.MovieDTO;
import br.texoit.application.dto.response.ServiceResponse;
import br.texoit.application.dto.response.SuccessResponse;
import br.texoit.application.enuns.MessageSystem;
import br.texoit.application.service.FilesService;
import br.texoit.application.service.MoviesService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class MoviesController {

    private final FilesService filesService;
    private final MoviesService moviesService;

    @PostMapping(value="/movies",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filmes registrados com sucesso"),
            @ApiResponse(code = 400, message = "Arquivo de formato inválido"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> register(@RequestParam("file") MultipartFile file) {

        if(!filesService.checkDocumentType(file))
            throw new EntityNotFoundException(MessageSystem.ERROR_INVALID_FILE.value());

        List<MovieDTO> movies = filesService.fileToEntity(file);

        if(movies.isEmpty())
            throw new EntityNotFoundException(MessageSystem.ERROR_CONVERT_FILE.value());

        ServiceResponse successDetails = moviesService.saveMovies(movies);

        if (!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @GetMapping(value="/movies/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filme recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Filme não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> getMovie(@PathVariable long id) {

        ServiceResponse successDetails = moviesService.getMovies(id);

        if (!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @PutMapping(value="/movies/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filme atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Filme não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> refreshMovie(@PathVariable long id,
                                                        @RequestBody MovieDTO movie) {

        ServiceResponse successDetails = moviesService.refreshMovie(id, movie);

        if (!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @DeleteMapping(value="/movies/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filme deletado com sucesso"),
            @ApiResponse(code = 404, message = "Filme não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> deleteMovie(@PathVariable long id) {

        ServiceResponse successDetails = moviesService.deleteMovie(id);

        if (!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @GetMapping(value="/movies/ranking",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Intervalo de prêmios recuperado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<Object> receive() {

        ServiceResponse successDetails = moviesService.prizeRange();

        if (!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(successDetails.getData());
    }
}
