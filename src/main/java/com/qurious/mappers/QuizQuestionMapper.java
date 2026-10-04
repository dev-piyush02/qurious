package com.qurious.qurious.mappers;

import com.qurious.qurious.DTO.QuestionDTO;
import com.qurious.qurious.entity.QuizQuestion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses={AnswerOptionMapper.class})
public interface QuizQuestionMapper {
    QuestionDTO quizQuestionToQuestionDTO(QuizQuestion quizQuestion);
    QuizQuestion questionDTOToQuizQuestion(QuestionDTO questionDTO);
    // for question list
    List<QuestionDTO> quizQuestionListToQuestionDTOList(List<QuizQuestion> quizQuestionList);
    List<QuizQuestion> quizQuestionListToQuizQuestionList(List<QuestionDTO> questionDTOList);
}
