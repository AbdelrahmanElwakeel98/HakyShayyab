package com.abdelrahman.irihackathon.QuestionSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelrahman.irihackathon.Adapter.AnswerAdapter;
import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Model.Answer;
import com.abdelrahman.irihackathon.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionCardActivity extends AppCompatActivity {

    private TextView txtQuestion;
    private ImageView back;
    private FloatingActionButton add;

    private RecyclerView list;
    private RequestQueue mQueue;
    private ArrayList<Answer> answers;
    private AnswerAdapter answerAdapter;

    private String question, questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_card);

        list = findViewById(R.id.answers_list);
        txtQuestion = findViewById(R.id.txt_question_display);
        back = findViewById(R.id.back);
        add = findViewById(R.id.btn_add);

        question = Global.selectedQuestion.getQuestion();
        questionId = Global.selectedQuestion.getId();

        if (Global.userType.equals("Bedouin")){
            add.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(QuestionCardActivity.this, AddAnswerActivity.class));
                    finish();
                }
            });
        }

        txtQuestion.setText(question);

        mQueue = Volley.newRequestQueue(this);
        answers = new ArrayList<>();

        jsonParse(questionId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestionCardActivity.this, QuestionsActivity.class));
                finish();
            }
        });

    }
    private void jsonParse(String questionId) {

        String url = Constants.API_URL + "answers/getAnswers/" + questionId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");



                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject question = jsonArray.getJSONObject(i);

                                Answer answer = new Answer();
                                answer.setAnswer(question.getString("answer"));
                                answer.setRating(question.getString("rating"));
                                answer.setAddedBy(question.getString("addedBy"));
                                answer.setAnswerID(question.getString("answerID"));

                                answers.add(answer);
                            }

                            answerAdapter = new AnswerAdapter(QuestionCardActivity.this, answers);
                            list.setLayoutManager(new LinearLayoutManager(QuestionCardActivity.this));
                            list.setAdapter(answerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuestionCardActivity.this, QuestionsActivity.class));
        finish();
    }
}
