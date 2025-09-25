package com.example.pravoslaviebg.models.game.question;

import java.util.List;

public class QuestionModel {

    private int Id;


    private String QuestionText;
    private List<AnswerModel> Answers;

    public int getId() { return Id; }
    public String getQuestionText() { return QuestionText; }
    public List<AnswerModel> getAnswers() { return Answers; }

    public int getCorrectOptionNumber() {

        return 1; // Placeholder until backend provides this data
    }
}
