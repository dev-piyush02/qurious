package com.qurious.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurious.DTO.AIRespDTO;
import com.qurious.DTO.AiRespQuesDTO;
import com.qurious.DTO.AnswerOptionDTO;
import com.qurious.DTO.QuestionDTO;
import com.qurious.entity.AnswerOption;
import com.qurious.entity.Quiz;
import com.qurious.entity.QuizQuestion;
import com.qurious.repository.QuestionRepo;
import com.qurious.repository.QuizRepo;
import com.qurious.utils.GlobalJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepo questionRepo;
    private final QuizRepo quizRepo;
    private final AICommnService aiCommnService;
    private final GlobalJsonParser globalJsonParser;

    QuestionService(QuestionRepo questionRepo, QuizRepo quizRepo,AICommnService aiCommnService, GlobalJsonParser globalJsonParser) {
        this.questionRepo = questionRepo;
        this.quizRepo = quizRepo;
        this.aiCommnService = aiCommnService;
        this.globalJsonParser = globalJsonParser;
    }

    public void generateQuestions(String prompt, Long quizId) throws JsonProcessingException {
        JsonNode respJson= aiCommnService.askAI(prompt);
        AIRespDTO dto = new AIRespDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.treeToValue(respJson, AIRespDTO.class);
            System.out.println(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Quiz quiz= quizRepo.findById(quizId).get();
        saveQuestionAnswer(dto, quiz);
    }

    @Transactional
    public void saveQuestionAnswer(AIRespDTO dto, Quiz quiz) {
        quiz.getQuestions().clear();

        for (AiRespQuesDTO questionDTO : dto.getQuestions()) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz(quiz); // Link Question to Quiz
            quizQuestion.setQuestionText(questionDTO.getQuestion());
            quizQuestion.setAnswerExplanation(questionDTO.getExplanation());
            quizQuestion.setCorrectAnswer(questionDTO.getAnswer());

            List<AnswerOption> optionsList = new ArrayList<>();
            for ( String answerOption : questionDTO.getOptions()) {
                AnswerOption ansOption = new AnswerOption();
                ansOption.setQuizQuestion(quizQuestion); // Link Option to Question
                ansOption.setOptText(answerOption);
                ansOption.setCorrect(answerOption.equals(questionDTO.getAnswer()));
                optionsList.add(ansOption);
            }
            quizQuestion.setOptions(optionsList);
            quiz.getQuestions().add(quizQuestion); // Add to Quiz list
        }
        // Because of CascadeType.ALL, this one line saves the Quiz,
        // ALL Questions, and ALL Options in one go.
        quizRepo.save(quiz);
    }



}
