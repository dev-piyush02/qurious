package com.qurious.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.util.List;

@Data
public class AIRespDTO {
        @JsonAlias({"questions", "mcqs"})
        private List<AiRespQuesDTO> questions;
}
