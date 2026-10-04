package com.qurious.qurious.controller;

import com.qurious.qurious.DTO.QuizRoomDTO;
import com.qurious.qurious.DTO.QuizRoomReqDTO;
import com.qurious.qurious.entity.Quiz;
import com.qurious.qurious.entity.QuizRoom;
import com.qurious.qurious.entity.User;
import com.qurious.qurious.repository.QuizRepo;
import com.qurious.qurious.repository.QuizRoomRepo;
import com.qurious.qurious.service.QuizRoomService;
import com.qurious.qurious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-room")
public class QuizRoomController {

    private final QuizRoomService quizRoomService;

    QuizRoomController(QuizRoomService quizRoomService) {
        this.quizRoomService = quizRoomService;
    }

    // To create a quiz room
    @PostMapping("create-room")
    public ResponseEntity<?> createQuizRoom(@RequestParam Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try {
            QuizRoomReqDTO quizRoomReqDTO = quizRoomService.createQuizRoom(quizId, userId);
            return new ResponseEntity<>(quizRoomReqDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // To find quiz room by quizRoomCode or quizRoomId
    @PostMapping("/find-quiz-room")
    public ResponseEntity<?> findQuizRoom(@RequestBody QuizRoomReqDTO quizRoomReqDTO) {
        QuizRoomDTO quizRoom = quizRoomService.findQuizRoom(quizRoomReqDTO);
        if(quizRoom.getRoomId()!=null){
            return new ResponseEntity<>(quizRoom, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // To find all quiz room by hostId
    @GetMapping("/find-my-quiz-room")
    public ResponseEntity<?> findAllQuizByHostId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try {
            List<QuizRoomDTO> quizRoomList = quizRoomService.findAllQuizRoomByHostId(userId);
            return new ResponseEntity<>(quizRoomList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // To find all active quiz rooms
    @GetMapping("/find-all-active-quiz-room")
    public ResponseEntity<?> findAllActiveQuizRooms() {
        try{
            List<QuizRoomDTO> quizRoomList = quizRoomService.findALlActiveQuizRoom();
            return new ResponseEntity<>(quizRoomList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // To deactivate a quiz room
    @PostMapping("/{quizRoomId}/deactivate-quiz-room")
    public ResponseEntity<?> deactivateQuizRoomById(@PathVariable String quizRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        QuizRoomReqDTO quizRoomReqDTO = new QuizRoomReqDTO();
        quizRoomReqDTO.setRoomId(quizRoomId);
        try{
            quizRoomService.deactivateQuizRoom(quizRoomReqDTO, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To join a quizRoom
    @PostMapping("/{quizRoomId}/join")
    public ResponseEntity<?> joinQuizRoom(@PathVariable String quizRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        try{
            quizRoomService.joinQuizRoom(quizRoomId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
