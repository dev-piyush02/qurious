package com.qurious.qurious.controller;

import com.qurious.qurious.DTO.SubmitAnswerDTO;
import com.qurious.qurious.entity.AnswerOption;
import com.qurious.qurious.service.AnswerSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
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
            answerSubmissionService.submitAnswer(answer, quizRoomId, username);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
