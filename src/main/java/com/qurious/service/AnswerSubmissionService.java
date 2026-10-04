package com.qurious.qurious.service;

import com.qurious.qurious.DTO.ResultDTO;
import com.qurious.qurious.DTO.SubmitAnswerDTO;
import com.qurious.qurious.entity.*;
import com.qurious.qurious.enums.QuizAttemptStatus;
import com.qurious.qurious.repository.*;
import com.qurious.qurious.utils.CalcDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AnswerSubmissionService {
    private final QuizRoomRepo quizRoomRepo;
    private final UserRepo userRepo;
    private final AnswerSubmissionRepo answerSubmissionRepo;
    private final AttemptRepo attemptRepo;
    private final CalcDuration calcDuration;
    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;

    AnswerSubmissionService(QuizRoomRepo quizRoomRepo,UserRepo userRepo, AnswerSubmissionRepo answerSubmissionRepo,
                            AttemptRepo attemptRepo, CalcDuration calcDuration, QuestionRepo questionRepo, AnswerOptionRepo answerOptionRepo) {
        this.quizRoomRepo = quizRoomRepo;
        this.userRepo = userRepo;
        this.answerSubmissionRepo = answerSubmissionRepo;
        this.attemptRepo = attemptRepo;
        this.calcDuration = calcDuration;
        this.questionRepo = questionRepo;
        this.answerOptionRepo = answerOptionRepo;
    }

    public boolean submitAnswer(SubmitAnswerDTO answer, String quizRoomId, String participantId) {

        QuizRoom quizRoom = quizRoomRepo.findById(quizRoomId).get();
        User participant = userRepo.findById(participantId).get();
        QuizAttempt quizAttempt = attemptRepo.findById(answer.getAttemptId()).get();
        if(quizAttempt.getStatus().equals(QuizAttemptStatus.IN_PROGRESS)){
            AnswerSubmission answerSubmission = new AnswerSubmission();
            answerSubmission.setQuizRoom(quizRoom);
            QuizQuestion quizQuestion = questionRepo.findById(answer.getQuestionId()).get();
            answerSubmission.setQuizQuestion(quizQuestion);
            answerSubmission.setParticipant(participant);
            AnswerOption answerOption = answerOptionRepo.findById(answer.getAnswerId()).get();
            answerSubmission.setSelectedAnswer(answerOption.getOptText());
            answerSubmission.setCorrect(answerOption.isCorrect() && answerOption.getQuizQuestion().getQuestionText().equals(quizQuestion.getQuestionText()));
            answerSubmission.setSubmittedAt(new Timestamp(System.currentTimeMillis()));
            answerSubmissionRepo.save(answerSubmission);

            return answerOption.isCorrect();
        }
        return false;
    }

    public ResultDTO generateResult(String quizRoomId, String participantId, Long attemptId) {
        QuizAttempt attempt= attemptRepo.findById(attemptId).get();
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setTotalCorrectAnswers(answerSubmissionRepo.countByParticipant_UserIdAndQuizRoom_RoomIdAndIsCorrectIsTrue(participantId, quizRoomId));
        resultDTO.setScore((double)resultDTO.getTotalCorrectAnswers());
        Long millis= (attempt.getSubmittedAt().getTime()-attempt.getStartedAt().getTime());
        resultDTO.setTotalTimeTaken(calcDuration.calcDurationString(millis)+" Minutes");
        return resultDTO;
    }
}
