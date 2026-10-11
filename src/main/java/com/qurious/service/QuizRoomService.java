package com.qurious.service;

import com.qurious.DTO.QuizRoomDTO;
import com.qurious.DTO.QuizRoomReqDTO;
import com.qurious.entity.Quiz;
import com.qurious.entity.QuizParticipants;
import com.qurious.entity.QuizRoom;
import com.qurious.entity.User;
import com.qurious.mappers.QuizRoomMapper;
import com.qurious.repository.ParticipantRepo;
import com.qurious.repository.QuizRepo;
import com.qurious.repository.QuizRoomRepo;
import com.qurious.repository.UserRepo;
import com.qurious.utils.QuizRoomCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class QuizRoomService {

    private final QuizRoomRepo quizRoomRepo;
    private final UserRepo userRepo;
    private final QuizRepo quizRepo;
    private final QuizRoomMapper quizRoomMapper;
    private final QuizRoomCodeGenerator quizRoomCodeGenerator;
    private final ParticipantRepo participantRepo;

    QuizRoomService(QuizRoomRepo quizRoomRepo, UserRepo userRepo, QuizRepo quizRepo, QuizRoomMapper quizRoomMapper,
                    QuizRoomCodeGenerator quizRoomCodeGenerator, ParticipantRepo participantRepo ) {
        this.quizRoomRepo = quizRoomRepo;
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.quizRoomMapper = quizRoomMapper;
        this.quizRoomCodeGenerator = quizRoomCodeGenerator;
        this.participantRepo = participantRepo;
    }


    // Create a new quizRoom
    public QuizRoomReqDTO createQuizRoom(Long quizId, String userId) {
        QuizRoom quizRoom = new QuizRoom();
        Quiz quiz = quizRepo.findById(quizId).get();
        quizRoom.setQuiz(quiz);
        User user = userRepo.findById(userId).get();
        quizRoom.setQuizRoomHost(user);
        quizRoom.setRoomCode(quizRoomCodeGenerator.generate());
        quizRoom.setRoomStatus("ACTIVE");
        quizRoom.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        quizRoomRepo.save(quizRoom);
        QuizRoomReqDTO quizRoomReqDTO = new QuizRoomReqDTO();
        BeanUtils.copyProperties(quizRoom, quizRoomReqDTO);
        return quizRoomReqDTO;
    }

    // Find a quizRoom using roomId or roomCode
    public QuizRoomDTO findQuizRoom(QuizRoomReqDTO quizRoomReqDTO) {
        if(quizRoomReqDTO.getRoomId()!=null){
            QuizRoom quizRoom= quizRoomRepo.findById(quizRoomReqDTO.getRoomId()).get();
            return quizRoomMapper.quizRoomToQuizRoomDTO(quizRoom);
        }
        else if(quizRoomReqDTO.getRoomCode()!=null){
            QuizRoom quizRoom= quizRoomRepo.findQuizRoomByRoomCode(quizRoomReqDTO.getRoomCode());
            return quizRoomMapper.quizRoomToQuizRoomDTO(quizRoom);
        }
        return new QuizRoomDTO();
    }

    // For fetching all active quizRooms
    public List<QuizRoomDTO> findALlActiveQuizRoom() {
        List<QuizRoom> quizRoomList= quizRoomRepo.findQuizRoomByRoomStatus("ACTIVE");
        return quizRoomMapper.quizRoomListToQuizRoomDTOList(quizRoomList);
    }

    // The host can see all his quizRooms
    public List<QuizRoomDTO> findAllQuizRoomByHostId(String hostId) {
        List<QuizRoom> quizRoomList= quizRoomRepo.findAllQuizRoomByQuizRoomHost_UserId(hostId);
        return quizRoomMapper.quizRoomListToQuizRoomDTOList(quizRoomList);
    }

    // Method to deactivate quiz after set timeout
    public void deactivateQuizRoom(QuizRoomReqDTO quizRoomReqDTO, String userId) {
        QuizRoom quizRoom= quizRoomRepo.findById(quizRoomReqDTO.getRoomId()).get();
        if(quizRoom.getRoomCode()!=null && quizRoom.getQuizRoomHost().getUserId().equals(userId)) {
            quizRoom.setRoomStatus("INACTIVE");
            quizRoomRepo.save(quizRoom);
        }
    }

    // Method to join an active quizRoom
    public void joinQuizRoom(String quizRoomId, String userId) {
        QuizRoom quizRoom= quizRoomRepo.findById(quizRoomId).get();
        if(quizRoom.getQuiz().getTotalQuestions()!=0 && quizRoom.getRoomStatus().equals("ACTIVE") &&
                quizRoom.getQuiz().getStatus().equals("ACTIVE")) {
            User participant = userRepo.findById(userId).get();
            QuizParticipants quizParticipant= new QuizParticipants();
            quizParticipant.setQuizRoom(quizRoom);
            quizParticipant.setParticipant(participant);
            quizParticipant.setJoinedAt(new Timestamp(System.currentTimeMillis()));
            quizParticipant.setCompleted(false);
            participantRepo.save(quizParticipant);
        }
    }
}
