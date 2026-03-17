package com.qurious.qurious.mappers;

import com.qurious.qurious.DTO.QuizDTO;
import com.qurious.qurious.entity.Quiz;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses={QuizQuestionMapper.class, UserMapper.class})
public interface QuizMapper {
    QuizDTO quizToQuizDTO(Quiz quiz);
    Quiz quizDTOToQuiz(QuizDTO quizDTO);

    List<QuizDTO> quizListToQuizDTOList(List<Quiz> quizList);
    List<Quiz> quizListDTOToQuizList(List<QuizDTO> quizDTOList);
}
