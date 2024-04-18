package br.texoit.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieDTO {
    @JsonProperty("year")
    private String year;
    @JsonProperty("title")
    private String title;
    @JsonProperty("studios")
    private String studios;
    @JsonProperty("producer")
    private String producer;
    @JsonProperty("winner")
    private String winner;
}
