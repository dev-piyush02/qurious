package com.qurious.mappers;

import com.qurious.DTO.AnswerOptionDTO;
import com.qurious.DTO.QuestionDTO;
import com.qurious.entity.AnswerOption;
import com.qurious.entity.QuizQuestion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ManualMappers {
    public QuestionDTO mapQuestionToQuestionDTO( QuizQuestion ques){
        QuestionDTO quesDTO= new QuestionDTO();
        quesDTO.setQuestionId(ques.getQuestionId());
        quesDTO.setQuestionText(ques.getQuestionText());
        quesDTO.setQuestionType(ques.getQuestionType());
        quesDTO.setOptions(mapAnswerOptionListToAnswerOptionDTOList(ques.getOptions()));
        return quesDTO;
    }
    public List<AnswerOptionDTO> mapAnswerOptionListToAnswerOptionDTOList(List<AnswerOption> ansOptList){
        List<AnswerOptionDTO> ansOptDTOList= new ArrayList<>();
        for(AnswerOption ansOpt:ansOptList){
            AnswerOptionDTO ansOptDTO= new AnswerOptionDTO();
            ansOptDTO.setOptId(ansOpt.getOptId());
            ansOptDTO.setOptText(ansOpt.getOptText());
            ansOptDTOList.add(ansOptDTO);
        }
        return ansOptDTOList;
    }
}
