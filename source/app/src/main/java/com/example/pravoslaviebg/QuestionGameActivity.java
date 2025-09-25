package com.example.pravoslaviebg;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pravoslaviebg.models.game.question.QuestionModel;
import com.example.pravoslaviebg.repositories.QuestionRepository;
import com.example.pravoslaviebg.services.game.QuestionService;
import com.example.pravoslaviebg.services.game.QuestionServiceInterface;

import java.util.Arrays;

public class QuestionGameActivity extends AppCompatActivity {
    private QuestionServiceInterface gameService;

    private TextView questionLabel;
    private final Button[] answerButtons = new Button[3];
    private TextView scoreLabel;
    private Button nextButton, resetGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_game);

        QuestionRepository repository = new QuestionRepository();
        gameService = new QuestionService(repository);

        initViews();

        gameService.getQuestionsLiveData().observe(this, questionList -> {
            if (questionList != null && !questionList.isEmpty()) {
                gameService.prepareQuestions(questionList);
                displayQuestion();
            }
        });

        gameService.loadQuestions();
    }

    private void initViews() {
        questionLabel = findViewById(R.id.questionLabel);
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        scoreLabel = findViewById(R.id.scoreLabel);
        nextButton = findViewById(R.id.nextButton);
        resetGame = findViewById(R.id.resetGame);

        for (Button btn : answerButtons) btn.setOnClickListener(this::onAnswerClicked);
        nextButton.setOnClickListener(this::onNextClicked);
        resetGame.setOnClickListener(this::onResetClicked);
    }

    private void displayQuestion() {
        if (gameService.isGameFinished()) {
            questionLabel.setText("Край!");

            String scoreText = "Score: " +
                    gameService.getScore() +
                    "/" +
                    gameService.getTotalQuestions();

            scoreLabel.setText(scoreText);
            scoreLabel.setVisibility(View.VISIBLE);

            for (Button btn : answerButtons) btn.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            resetGame.setVisibility(View.VISIBLE);
            return;
        }

        QuestionModel q = gameService.nextQuestion();
        if (q == null) return;

        questionLabel.setText(q.getQuestionText());
        for (int i = 0; i < 3; i++) {
            answerButtons[i].setText(q.getAnswers().get(i).getAnswerText());
            answerButtons[i].setEnabled(true);
            answerButtons[i].setBackgroundColor(Color.GRAY);
            answerButtons[i].setTextColor(Color.BLACK);
        }

        nextButton.setVisibility(View.GONE);
    }

    private void onAnswerClicked(View view) {
        Button selectedButton = (Button) view;
        int selectedIndex = Arrays.asList(answerButtons).indexOf(selectedButton) + 1;
        int correct = gameService.getCorrectOptionNumber();

        if (selectedIndex == correct) {
            selectedButton.setBackgroundColor(Color.GREEN);
            selectedButton.setTextColor(Color.WHITE);
            gameService.incrementScore();
        } else {
            for (int i = 0; i < answerButtons.length; i++) {
                if (i + 1 != correct) {
                    answerButtons[i].setBackgroundColor(Color.RED);
                    answerButtons[i].setTextColor(Color.WHITE);
                }
            }
            answerButtons[correct - 1].setBackgroundColor(Color.GREEN);
            answerButtons[correct - 1].setTextColor(Color.WHITE);
        }

        disableAnswerButtons();
        nextButton.setVisibility(View.VISIBLE);
    }

    private void onNextClicked(View view) {
        displayQuestion();
    }

    private void onResetClicked(View view) {
        scoreLabel.setVisibility(View.GONE);
        for (Button btn : answerButtons) btn.setVisibility(View.VISIBLE);

        nextButton.setVisibility(View.GONE);
        resetGame.setVisibility(View.GONE);

        gameService.resetGame();
    }

    private void disableAnswerButtons() {
        for (Button btn : answerButtons) btn.setEnabled(false);
    }
}
