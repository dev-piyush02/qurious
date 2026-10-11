package com.qurious.mappers;

import com.qurious.DTO.AnswerOptionDTO;
import com.qurious.entity.AnswerOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerOptionMapper {
    AnswerOptionDTO answerOptionToAnswerOptionDTO(AnswerOption answerOption);
    AnswerOption answerOptionDTOToAnswerOption(AnswerOptionDTO answerOptionDTO);
}
