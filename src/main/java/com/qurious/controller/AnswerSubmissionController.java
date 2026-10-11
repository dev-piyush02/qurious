package com.qurious.controller;

import com.qurious.DTO.SubmitAnswerDTO;
import com.qurious.service.AnswerSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/submit")
@Slf4j
public class AnswerSubmissionController {

    private final AnswerSubmissionService answerSubmissionService;
    AnswerSubmissionController(AnswerSubmissionService answerSubmissionService) {
        this.answerSubmissionService = answerSubmissionService;
    }


    @PostMapping("/{quizRoomId}/answer")
    public ResponseEntity<?> submitAnswer(@PathVariable String quizRoomId, @RequestBody SubmitAnswerDTO answer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            Boolean isCorrect= answerSubmissionService.submitAnswer(answer, quizRoomId, username);
            log.debug("Answer submitted by user {} for quiz {} and question {} is {}", username, answer.getQuizId(), answer.getQuestionId(), isCorrect);
            return new ResponseEntity<>(isCorrect, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
