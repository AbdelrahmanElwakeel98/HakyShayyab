package com.abdelrahman.irihackathon.QuestionSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abdelrahman.irihackathon.Adapter.QuestionAdapter;
import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.RecyclerItemClickListener;
import com.abdelrahman.irihackathon.DashboardActivity;
import com.abdelrahman.irihackathon.Model.Question;
import com.abdelrahman.irihackathon.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    private MaterialSpinner spinner;
    private FloatingActionButton add;
    private ImageView back;

    private ArrayList<String> category;
    private RecyclerView list;
    private RequestQueue mQueue;
    private ArrayList<Question> questions;
    private QuestionAdapter questionAdapter;
    private String userType, userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        list = findViewById(R.id.questions_list);
        add = findViewById(R.id.btn_add);
        spinner = findViewById(R.id.spinner);
        back = findViewById(R.id.back);


        category = new ArrayList<>();

        category.add(getResources().getString(R.string.select_category));
        category.add(getResources().getString(R.string.manual_dashboard_songs));
        category.add(getResources().getString(R.string.manual_dashboard_medicine));
        category.add(getResources().getString(R.string.manual_dashboard_clothing));
        category.add(getResources().getString(R.string.manual_dashboard_cooking));
        category.add(getResources().getString(R.string.manual_dashboard_handcrafting));

        spinner.setItems(category);

        mQueue = Volley.newRequestQueue(this);
        questions = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestionsActivity.this, DashboardActivity.class));
                finish();
            }
        });

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0) {
                    jsonParse(category.get(position));
                    Global.categoryQuestion = category.get(position);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.categoryQuestion.equals(getResources().getString(R.string.select_category))) {
                    Intent intent = new Intent(QuestionsActivity.this, AddQuestionsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        list.addOnItemTouchListener(
                new RecyclerItemClickListener(QuestionsActivity.this, list ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(QuestionsActivity.this, QuestionCardActivity.class);
                        Global.selectedQuestion = questions.get(position);
                        startActivity(intent);
                        finish();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void jsonParse(String categoryId) {

        String url = Constants.API_URL + "questions/getQuestions/" + categoryId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            questions.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject question = jsonArray.getJSONObject(i);

                                Question q = new Question();
                                q.setId(question.getString("questionID"));
                                q.setAddedOn(question.getString("addedOn"));
                                q.setAddedBy(question.getString("addedBy"));
                                q.setQuestion(question.getString("question"));

                                questions.add(q);
                            }

                            questionAdapter = new QuestionAdapter(QuestionsActivity.this, questions);
                            list.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
                            list.setAdapter(questionAdapter);

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
        startActivity(new Intent(QuestionsActivity.this, DashboardActivity.class));
        finish();
    }
}
