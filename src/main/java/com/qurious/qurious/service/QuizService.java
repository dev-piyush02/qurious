package com.qurious.qurious.service;

import com.qurious.qurious.DTO.AnswerOptionDTO;
import com.qurious.qurious.DTO.QuestionDTO;
import com.qurious.qurious.DTO.QuizDTO;
import com.qurious.qurious.DTO.ResultDTO;
import com.qurious.qurious.entity.*;
import com.qurious.qurious.enums.QuizAttemptStatus;
import com.qurious.qurious.mappers.QuizMapper;
import com.qurious.qurious.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepo quizRepo;
    private final QuizMapper quizMapper;
    private final UserRepo userRepo;
    private final QuizRoomRepo quizRoomRepo;
    private final AttemptRepo attemptRepo;
    private final AnswerSubmissionService answerSubmissionService;
    private final QuizParticipantRepo quizParticipantRepo;

    QuizService(QuizRepo quizRepo, QuizMapper quizMapper, UserRepo userRepo, QuizRoomRepo quizRoomRepo, AttemptRepo attemptRepo,
                AnswerSubmissionService answerSubmissionService,  QuizParticipantRepo quizParticipantRepo) {
        this.quizRepo = quizRepo;
        this.quizMapper = quizMapper;
        this.userRepo = userRepo;
        this.quizRoomRepo = quizRoomRepo;
        this.attemptRepo = attemptRepo;
        this.answerSubmissionService = answerSubmissionService;
        this.quizParticipantRepo = quizParticipantRepo;
    }
    // Create a new quiz
    public Boolean createQuiz(Quiz quiz) {
        try {
            quiz.setCreationTime(Timestamp.from(Instant.now()));
            quiz.setStatus("ACTIVE");
            quizRepo.save(quiz);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    // Fetch all quizzes and mapped questions and answers available
    public List<QuizDTO> findAllByUserId(String userId){
        List<Quiz> quizList= quizRepo.findAllByCreatedBy_UserId(userId);
        return quizMapper.quizListToQuizDTOList(quizList);
    }

    // Find a quiz and mapped questions and answer options by quizId
    public QuizDTO findQuizById(Long id) {
        Quiz quiz= quizRepo.findById(id).get();
        QuizDTO quizDTO= quizMapper.quizToQuizDTO(quiz);
        return quizDTO;
    }

    // Fetch all active quizzes with questions and answer options
    public List<QuizDTO> findAllActiveQuiz(){
        List<Quiz> quizList= quizRepo.findAllByStatus("ACTIVE");
        List<QuizDTO> quizDTOList=new ArrayList<>();
        for (Quiz quiz:quizList){
            QuizDTO quizDTO=new QuizDTO();
            BeanUtils.copyProperties(quiz,quizDTO);
            List<QuestionDTO> questionDTOList=new ArrayList<>();
            for(QuizQuestion ques:quiz.getQuestions()){
                QuestionDTO questionDTO=new QuestionDTO();
                BeanUtils.copyProperties(ques,questionDTO);
                questionDTOList.add(questionDTO);
                List<AnswerOptionDTO> answerOptionDTOList=new ArrayList<>();
                for(AnswerOption answerOption:ques.getOptions()){
                    AnswerOptionDTO answerOptionDTO=new AnswerOptionDTO();
                    BeanUtils.copyProperties(answerOption,answerOptionDTO);
                    answerOptionDTOList.add(answerOptionDTO);
                }
                questionDTO.setOptions(answerOptionDTOList);
            }
            quizDTO.setQuestions(questionDTOList);
            quizDTOList.add(quizDTO);
        }
        return quizDTOList;
    }

    // Method to deactivate quiz
    public Boolean deleteQuiz(Long id, String userId) {
        Quiz quiz = quizRepo.findById(id).get();
        if(quiz.getQuizId()!=null && quiz.getCreatedBy().getUserId().equals(userId)) {
            quizRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Method to deactivate quiz
    public Boolean deactivateQuiz(Long id, String userId){
        Quiz quiz = quizRepo.findById(id).get();
        if(quiz.getQuizId()!=null && quiz.getCreatedBy().getUserId().equals(userId)) {
            quiz.setStatus("INACTIVE");
            quizRepo.save(quiz);
            return true;
        }
        return false;
    }

    // Method to start a quiz
    public Long startQuiz(String quizRoomId, Long quizId, String userId) {
        Quiz quiz = quizRepo.findById(quizId).get();
        QuizRoom quizRoom= quizRoomRepo.findById(quizRoomId).get();
        User participant= userRepo.findById(userId).get();
        if(quizParticipantRepo.findByParticipant_UserIdAndQuizRoom_RoomId(userId, quizRoomId)!=null) {
            QuizAttempt quizAttempt = new QuizAttempt();
            quizAttempt.setQuiz(quiz);
            quizAttempt.setQuizRoom(quizRoom);
            quizAttempt.setUser(participant);
            quizAttempt.setStartedAt(Timestamp.from(Instant.now()));
            quizAttempt.setStatus(QuizAttemptStatus.IN_PROGRESS);
            QuizAttempt attempt = attemptRepo.save(quizAttempt);
            return attempt.getAttemptId();
        }
        return null;
    }

    // Method to submit quiz
    public ResultDTO submitQuiz(Long attemptId, String userId, String quizRoomId) {
        QuizAttempt quizAttempt= attemptRepo.findById(attemptId).get();
        quizAttempt.setStatus(QuizAttemptStatus.FINISHED);
        quizAttempt.setSubmittedAt(Timestamp.from(Instant.now()));
        ResultDTO result= answerSubmissionService.generateResult(quizRoomId, userId, attemptId);
        quizAttempt.setScore(result.getScore());
        attemptRepo.save(quizAttempt);
        return result;
    }
}
