package com.qurious.mappers;

import com.qurious.DTO.QuizRoomDTO;
import com.qurious.entity.QuizRoom;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses={QuizMapper.class, UserMapper.class})
public interface QuizRoomMapper {
    QuizRoomDTO quizRoomToQuizRoomDTO(QuizRoom quizRoom);
    QuizRoom quizRoomDTOToQuizRoom(QuizRoomDTO quizRoomDTO);

    List<QuizRoom> quizRoomDTOListToQuizRoomList(List<QuizRoomDTO> quizRoomDTOList);
    List<QuizRoomDTO> quizRoomListToQuizRoomDTOList(List<QuizRoom> quizRoomList);
}
