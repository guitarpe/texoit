import br.texoit.application.controller.MoviesController;
import br.texoit.application.dto.request.MovieDTO;
import br.texoit.application.dto.response.ProducersDTO;
import br.texoit.application.dto.response.ServiceResponse;
import br.texoit.application.dto.response.SuccessResponse;
import br.texoit.application.service.FilesService;
import br.texoit.application.service.MoviesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoviesControllerTest {

    @Mock
    private FilesService filesService;

    @Mock
    private MoviesService moviesService;

    @InjectMocks
    private MoviesController moviesController;

    private static final String CSV_FILE_PATH = "src/test/resources/test-movies.csv";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegister() throws IOException {

        Path path = Paths.get(CSV_FILE_PATH);
        String name = "test-movies.csv";
        String originalFileName = "test-movies.csv";
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(path);
        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

        when(filesService.checkDocumentType(multipartFile)).thenReturn(true);
        when(filesService.fileToEntity(multipartFile)).thenReturn(Collections.singletonList(MovieDTO.builder().build()));

        when(moviesService.saveMovies(anyList())).thenReturn(ServiceResponse.builder()
                .status(true)
                .message("Success")
                .data(null)
                .build());

        ResponseEntity<SuccessResponse> responseEntity = moviesController.register(multipartFile);

        verify(filesService, times(1)).checkDocumentType(multipartFile);
        verify(filesService, times(1)).fileToEntity(multipartFile);
        verify(moviesService, times(1)).saveMovies(anyList());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals("Success", responseEntity.getBody().getMessage());
        assertEquals(200, responseEntity.getBody().getCode());
        assertEquals(true, responseEntity.getBody().isSuccess());
        assertEquals(null, responseEntity.getBody().getData());
    }

    @Test
    public void testGetMovie() {
        long id = 1L;

        ServiceResponse successDetails = ServiceResponse.builder()
                .status(true)
                .message("Success")
                .data(MovieDTO.builder().build())
                .build();

        when(moviesService.getMovies(id)).thenReturn(successDetails);

        ResponseEntity<SuccessResponse> response = moviesController.getMovie(id);

        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(LocalDateTime.class, response.getBody().getTimestamp().getClass());
        assertEquals(MovieDTO.class, response.getBody().getData().getClass());
    }

    @Test
    public void testRefreshMovie() {
        long id = 1L;
        MovieDTO movieDTO = MovieDTO.builder().build();

        ServiceResponse successDetails = ServiceResponse.builder()
                .status(true)
                .message("Success")
                .data(movieDTO)
                .build();

        when(moviesService.refreshMovie(id, movieDTO)).thenReturn(successDetails);

        ResponseEntity<SuccessResponse> response = moviesController.refreshMovie(id, movieDTO);

        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(LocalDateTime.class, response.getBody().getTimestamp().getClass());
        assertEquals(MovieDTO.class, response.getBody().getData().getClass());
    }

    @Test
    public void testDeleteMovie() {
        long id = 1L;

        ServiceResponse successDetails = ServiceResponse.builder()
                .status(true)
                .message("Success")
                .data(null)
                .build();

        when(moviesService.deleteMovie(id)).thenReturn(successDetails);

        ResponseEntity<SuccessResponse> response = moviesController.deleteMovie(id);

        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(LocalDateTime.class, response.getBody().getTimestamp().getClass());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    public void testReceive() {
        ServiceResponse successDetails = ServiceResponse.builder()
                .status(true)
                .message("Success")
                .data(ProducersDTO.builder().build())
                .build();
        when(moviesService.prizeRange()).thenReturn(successDetails);

        ResponseEntity<Object> response = moviesController.receive();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, successDetails.isStatus());
        assertEquals("Success", successDetails.getMessage());
        assertEquals(null, successDetails.getData());
    }

}
