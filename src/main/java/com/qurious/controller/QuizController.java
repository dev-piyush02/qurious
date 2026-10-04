package com.qurious.qurious.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;
import com.qurious.qurious.DTO.QuizDTO;
import com.qurious.qurious.DTO.ResultDTO;
import com.qurious.qurious.entity.AIPrompt;
import com.qurious.qurious.entity.Quiz;
import com.qurious.qurious.entity.User;
import com.qurious.qurious.repository.QuizRepo;
import com.qurious.qurious.repository.UserRepo;
import com.qurious.qurious.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final UserRepo userRepo;

    QuizController(QuizService quizService, QuestionService questionService, UserRepo userRepo) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.userRepo = userRepo;
    }

    // To create quiz
    @PostMapping("/create-quiz")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        User user = userRepo.findById(userid).get();
        if (user.getUserId() != null) {
            quiz.setCreatedBy(user);
            if (quizService.createQuiz(quiz)) {
                return new ResponseEntity<>(quiz, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // To view quiz by quizID
    @GetMapping("/{quizId}/view-quiz")
    public ResponseEntity<?> viewQuizByQuizId(@PathVariable Long quizId) {
        try{
            QuizDTO quiz= quizService.findQuizById(quizId);
            return new ResponseEntity<>(quiz, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("No Quiz Available!", HttpStatus.NOT_FOUND);
        }
    }

    // To view all quizzes
    @GetMapping("/view-all-quiz")
    public ResponseEntity<?> viewAllQuiz() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        if (userRepo.findById(userid).get().getUserId() != null) {
            List<QuizDTO> quizzes = quizService.findAllByUserId(userid);
            return new ResponseEntity<>(quizzes, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // To view all active quizzes
    @GetMapping("/view-all-active-quiz")
    public ResponseEntity<?> viewAllActiveQuiz() {
        try{
            List<QuizDTO> quizDTOList= quizService.findAllActiveQuiz();
            if(quizDTOList.isEmpty()){
                return new ResponseEntity<>("No active Quizzes Found", HttpStatus.OK);
            }
            return new ResponseEntity<>(quizDTOList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // To generate quiz questions and answers
    @PostMapping("/{quizId}/gen-ques")
    public ResponseEntity<?> generateQuestions(@RequestBody AIPrompt aiPrompt, @PathVariable Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            questionService.generateQuestions(aiPrompt.getPrompt(), quizId);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    // To deactivate quiz
    @PostMapping("/{quizId}/deactivate-quiz")
    public ResponseEntity<?> deactivateQuiz(@PathVariable Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try{
            quizService.deactivateQuiz(quizId, userId);
            return new ResponseEntity<>("Quiz Deactivated", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // To delete quiz
    @PostMapping("/{quizId}/delete quiz")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try{
            quizService.deleteQuiz(quizId, userId);
            return new ResponseEntity<>("Quiz Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // To start the quiz
    @PostMapping("/attempt/{quizId}/start")//participant ka check lagana hai if joined room or not
    public ResponseEntity<?> startQuiz(@PathVariable Long quizId, @RequestParam String quizRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try {
            Long attemptId= quizService.startQuiz(quizRoomId, quizId, userId);
            return new ResponseEntity<>(attemptId, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // To submit quiz
    @PostMapping("/attempt/{attemptId}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable Long attemptId, @RequestParam String quizRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try{
            ResultDTO result= quizService.submitQuiz(attemptId, userId, quizRoomId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
