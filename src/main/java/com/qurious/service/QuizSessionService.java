package com.qurious.service;

import com.qurious.entity.*;
import com.qurious.enums.QuizAttemptStatus;
import com.qurious.mappers.ManualMappers;
import com.qurious.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.qurious.DTO.WSHelperDTOs.*;
import com.qurious.DTO.QuestionDTO;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class QuizSessionService {

    private final AttemptRepo attemptRepo;
    private final QuestionRepo questionRepo;
    private final ManualMappers manualMappers;
    private final QuizRepo quizRepo;
    private final QuizRoomRepo quizRoomRepo;
    private final UserRepo userRepo;
    private final QuizParticipantRepo quizParticipantRepo;
    private final AnswerOptionRepo answerOptionRepo;
    private final AnswerSubmissionRepo answerSubmissionRepo;

    public QuizSessionService(AttemptRepo attemptRepo, QuestionRepo questionRepo, ManualMappers manualMappers,
                              QuizRepo quizRepo, QuizRoomRepo quizRoomRepo, UserRepo userRepo, QuizParticipantRepo quizParticipantRepo, AnswerOptionRepo answerOptionRepo, AnswerSubmissionRepo answerSubmissionRepo) {
        this.attemptRepo = attemptRepo;
        this.questionRepo = questionRepo;
        this.manualMappers = manualMappers;
        this.quizRepo = quizRepo;
        this.quizRoomRepo = quizRoomRepo;
        this.userRepo = userRepo;
        this.quizParticipantRepo = quizParticipantRepo;
        this.answerOptionRepo = answerOptionRepo;
        this.answerSubmissionRepo = answerSubmissionRepo;
    }


    //Finally start the quiz
    public void start(String quizRoomId, Long quizId, String userId){
        Quiz quiz = quizRepo.findById(quizId).get();
        QuizRoom quizRoom= quizRoomRepo.findById(quizRoomId).get();
        User participant= userRepo.findById(userId).get();
        //Validations for quiz
        if(quiz.getStatus().equals("ACTIVE") && quizRoom.getRoomStatus().equals("ACTIVE")) {
            if (quizParticipantRepo.findByParticipant_UserIdAndQuizRoom_RoomId(userId, quizRoomId) != null) {
                QuizAttempt quizAttempt = new QuizAttempt();
                quizAttempt.setQuiz(quiz);
                quizAttempt.setQuizRoom(quizRoom);
                quizAttempt.setUser(participant);
                quizAttempt.setSeenQuesCnt(0);
                quizAttempt.setStartedAt(Timestamp.from(Instant.now()));
                quizAttempt.setStatus(QuizAttemptStatus.IN_PROGRESS);
                attemptRepo.save(quizAttempt);
                log.debug("User {} started the quiz {}.",userId, quiz.getQuizId());
            }
            log.error("User {} has already attempted the quiz {} earlier.",userId, quiz.getQuizId());
        }
        log.error("User {} couldn't start the quiz {} as the quiz is not active.",userId, quiz.getQuizId());
    }
    //Submit the answer
    public boolean submitAnswer(String quizRoomId, Long quizId, String userId, Long questionId, Long optionId){
        //quizAttempt se current question fetch kr lena and ALSO increment it by 1
        QuizAttempt quizAttempt = attemptRepo.findByUser_UserIdAndQuiz_QuizId(userId, quizId);
        QuizRoom quizRoom = quizRoomRepo.findById(quizRoomId).get();
        User participant = userRepo.findById(userId).get();
        if(quizAttempt.getStatus().equals(QuizAttemptStatus.IN_PROGRESS)){
            AnswerSubmission answerSubmission = new AnswerSubmission();
            answerSubmission.setQuizRoom(quizRoom);
            QuizQuestion quizQuestion = questionRepo.findById(questionId).get();
            answerSubmission.setQuizQuestion(quizQuestion);
            answerSubmission.setParticipant(participant);
            AnswerOption answerOption = answerOptionRepo.findById(optionId).get();
            answerSubmission.setSelectedAnswer(answerOption.getOptText());
            answerSubmission.setCorrect(answerOption.isCorrect() && answerOption.getQuizQuestion().getQuestionText()
                    .equals(quizQuestion.getQuestionText()));
            answerSubmission.setSubmittedAt(new Timestamp(System.currentTimeMillis()));
            answerSubmission.setQuizId(quizId);
            answerSubmissionRepo.save(answerSubmission);
            quizAttempt.setSeenQuesCnt(quizAttempt.getSeenQuesCnt() + 1);
            return answerOption.isCorrect();
        }
        return false;
    }
    //skip answering the question
    public boolean skip(String QuizRoomId, Long quizId, String userId, String questionId){
        //quizAttempt se current question fetch kr lena and ALSO increment it by 1

        return false;
    }
    //Get the next question (called by each WS controller)
    public Optional<QuestionDTO> nextQuestion(String QuizRoomId, Long quizId, String userId){
        int cnt= attemptRepo.findSeenQuesCnt(userId, quizId);
        QuizQuestion ques= questionRepo.findQuestionAtOffset(quizId, cnt).get();
        if(ques.getQuestionId()!=null) {
            return Optional.ofNullable(manualMappers.mapQuestionToQuestionDTO(ques));
        }
        return null;
    }
    public Optional<QuestionDTO> currentQuestion(String QuizRoomId, Long quizId, String userId){return null;} // no advance, for resume
    public QuizFinishedDto summary(String QuizRoomId, Long quizId, String userId){return null;}
}
