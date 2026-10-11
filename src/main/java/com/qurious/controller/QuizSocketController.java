package com.qurious.controller;

import com.qurious.DTO.QuestionDTO;
import com.qurious.DTO.SubmitAnswerDTO;
import com.qurious.DTO.WSHelperDTOs.*;
import com.qurious.service.QuizSessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
public class QuizSocketController {

    private final QuizSessionService quizSessionService;
    private final SimpMessagingTemplate messaging;

    public QuizSocketController(QuizSessionService quizSessionService,  SimpMessagingTemplate messaging) {
        this.quizSessionService = quizSessionService;
        this.messaging = messaging;
    }

    // Client sends to: /app/quiz/{quizId}/start
    @MessageMapping("/{quizRoom}/quiz/{quizId}/start")
    public void start(@DestinationVariable String quizRoomId, @DestinationVariable Long quizId, Principal principal) {
        String userId = principal.getName();
        quizSessionService.start(quizRoomId, quizId, userId);
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            log.error(e.getMessage());
        }
        sendNext(quizRoomId, quizId, userId); // sends next question from the quiz
    }

    // Client sends to: /app/quiz/{quizId}/answer
    @MessageMapping("/{quizRoom}/quiz/{quizId}/answer")
    public void answer(@DestinationVariable String quizRoomId, @DestinationVariable Long quizId,
                       @Payload @Valid SubmitAnswerDTO answer, Principal principal) {
        String userId = principal.getName();

        // Service should persist the answer ONLY if req.questionId matches the user's current question; otherwise ignore (duplicate / late / out-of-order).
        boolean accepted = quizSessionService.submitAnswer(quizRoomId, quizId, userId, answer.getQuestionId(), answer.getAnswerId());
        if (!accepted) {
            log.debug("Ignored stale answer user={} for question={}", userId, answer.getQuestionId());
            return;
        }
        sendNext(quizRoomId, quizId, userId);
    }

    // Client sends to: /app/quiz/{quizId}/skip
    @MessageMapping("/{quizRoom}/quiz/{quizId}/skip")
    public void skip(@DestinationVariable String quizRoomId, @DestinationVariable Long quizId, @Payload @Valid SkipRequest req,
                     Principal principal) {
        String userId = principal.getName();
        if (quizSessionService.skip(quizRoomId, quizId, userId, req.questionId())) {
            sendNext(quizRoomId, quizId, userId);
        }
    }

    // Client sends to: /app/quiz/{quizId}/resume  (after reconnect)
    @MessageMapping("/{quizRoom}/quiz/{quizId}/resume")
    public void resume(@DestinationVariable String quizRoomId, @DestinationVariable Long quizId, Principal principal) {
        sendCurrent(quizRoomId, quizId, principal.getName());
    }

    private void sendNext(String quizRoomId, Long quizId, String userId) {
        Optional<QuestionDTO> next = quizSessionService.nextQuestion(quizRoomId, quizId, userId);
        if (next.isPresent()) {
            messaging.convertAndSendToUser(userId, "/queue/question", next.get());
        } else {
            messaging.convertAndSendToUser(userId, "/queue/finished",
                    quizSessionService.summary(quizRoomId, quizId, userId));
        }
    }

    private void sendCurrent(String quizRoomId, Long quizId, String userId) {
        quizSessionService.nextQuestion(quizRoomId, quizId, userId)
                .ifPresentOrElse(
                        ques -> messaging.convertAndSendToUser(userId, "/queue/question", ques),
                        () -> messaging.convertAndSendToUser(userId, "/queue/finished",
                                quizSessionService.summary(quizRoomId, quizId, userId)));
    }

    // Errors go only to the offending user
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ErrorDto handle(Exception ex) {
        log.warn("WS error", ex);
        return new ErrorDto("BAD_REQUEST", ex.getMessage());
    }
}
