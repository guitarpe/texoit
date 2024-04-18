package br.texoit.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProducersDTO {
    @JsonProperty("min")
    public List<ProducerInfoDTO> min;

    @JsonProperty("max")
    public List<ProducerInfoDTO> max;
}
