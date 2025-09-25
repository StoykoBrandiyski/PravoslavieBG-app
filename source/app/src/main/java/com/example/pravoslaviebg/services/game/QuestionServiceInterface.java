package com.example.pravoslaviebg.services.game;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.game.question.QuestionModel;

import java.util.List;

public interface QuestionServiceInterface {

    MutableLiveData<List<QuestionModel>> getQuestionsLiveData() ;

    void loadQuestions();

    boolean isGameFinished();

    void prepareQuestions(List<QuestionModel> questionList);

    QuestionModel nextQuestion();

    int getSelectedQuestionIndex();

    int getScore();

    void incrementScore();

    void resetGame();

    int getCorrectOptionNumber();

    int getTotalQuestions();
}
