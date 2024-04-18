package br.texoit.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProducerInfoDTO {
    @JsonProperty("producer")
    private String producer;

    @JsonProperty("interval")
    private int interval;

    @JsonProperty("previousWin")
    private int previousWin;

    @JsonProperty("followingWin")
    private int followingWin;
}
