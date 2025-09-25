package com.example.pravoslaviebg.services.game;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.game.question.QuestionModel;
import com.example.pravoslaviebg.repositories.QuestionRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class QuestionService implements QuestionServiceInterface {
    private static final int TOTAL_QUESTIONS = 5;

    private final QuestionRepository questionRepository;
    private final Random random = new Random();
    private final MutableLiveData<List<QuestionModel>> questions = new MutableLiveData<>();
    private final Queue<Integer> selectedQuestionIds = new LinkedList<>();

    private int currentQuestionIndex = 0;
    private int previousQuestionId = -1;
    private int selectedQuestionIndex = 0;
    private int score = 0;

    public QuestionService(QuestionRepository repository) {
        this.questionRepository = repository;
    }

    public MutableLiveData<List<QuestionModel>> getQuestionsLiveData() {
        return questions;
    }

    public void loadQuestions() {
        questionRepository.getAllQuestions(questions);
    }

    public boolean isGameFinished() {
        return currentQuestionIndex >= TOTAL_QUESTIONS;
    }

    public void prepareQuestions(List<QuestionModel> questionList) {
        selectedQuestionIds.clear();
        while (selectedQuestionIds.size() < TOTAL_QUESTIONS) {
            int randomId = random.nextInt(questionList.size());
            if (randomId != previousQuestionId) {
                selectedQuestionIds.add(randomId);
                previousQuestionId = randomId;
            }
        }
    }

    public QuestionModel nextQuestion() {
        Integer polledId = selectedQuestionIds.poll();
        if (polledId == null) return null;

        selectedQuestionIndex = polledId;
        currentQuestionIndex++;
        return questions.getValue().get(selectedQuestionIndex);
    }

    public int getSelectedQuestionIndex() {
        return selectedQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public void resetGame() {
        currentQuestionIndex = 0;
        score = 0;
        previousQuestionId = -1;
        selectedQuestionIds.clear();
        loadQuestions();
    }

    public int getCorrectOptionNumber() {
        return questions.getValue().get(selectedQuestionIndex).getCorrectOptionNumber();
    }

    public int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }
}

