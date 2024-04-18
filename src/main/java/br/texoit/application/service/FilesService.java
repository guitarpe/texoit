package br.texoit.application.service;

import br.texoit.application.dto.request.MovieDTO;
import br.texoit.application.enuns.MessageSystem;
import br.texoit.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilesService {

    public List<MovieDTO> fileToEntity(MultipartFile file) {
        try {
            if (checkDocumentType(file)) {
                return convertCSV(file);
            }
        }catch (Exception ex){
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    public boolean checkDocumentType(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.endsWith(".csv")) {
            return true;
        }
        return false;
    }

    public List<MovieDTO> convertCSV(MultipartFile file) throws Exception {
        List<MovieDTO> movies = new ArrayList<>();
        Set<String> movieTitles = new HashSet<>();

        try {
            if (file == null) {
                throw new ResourceNotFoundException(MessageSystem.ERROR_FILE_CSV_NULL.value());
            }

            if (file.isEmpty()) {
                throw new IllegalArgumentException(MessageSystem.ERROR_FILE_CSV_EMPTY.value());
            }

            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader());

                parser.stream().forEach(register -> {
                    String title = register.get(1);

                    if (!movieTitles.contains(title)) {
                        movies.add(MovieDTO.builder()
                                .year(register.get(0))
                                .title(register.get(1))
                                .studios(register.get(2))
                                .producer(register.get(3))
                                .winner(register.get(4)).build());
                        movieTitles.add(title);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movies;

        } catch (Exception ex) {
            throw new IOException(MessageSystem.ERROR_READ_CSV_FILE.value(), ex);
        }
    }

}
