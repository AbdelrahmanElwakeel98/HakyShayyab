package com.abdelrahman.irihackathon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
import com.abdelrahman.irihackathon.Model.Answer;
import com.abdelrahman.irihackathon.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Answer> answers;
    private FirebaseAuth auth;

    public AnswerAdapter(Context context, ArrayList<Answer> answers){
        this.context = context;
        this.answers = answers;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_answers,parent,false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.ViewHolder holder, final int position) {

        holder.answer.setText(answers.get(position).getAnswer());
        holder.rating.setText(answers.get(position).getRating());

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRating(answers.get(position).getAnswerID(), answers.get(position).getRating(), "1"
                        , answers.get(position).getAddedBy(), position);
                /*answers.get(position).setRating(String.valueOf(Integer.valueOf(answers.get(position).getRating()) + 1));
                notifyDataSetChanged();*/
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRating(answers.get(position).getAnswerID(), answers.get(position).getRating(), "-1"
                        , answers.get(position).getAddedBy(), position);
                /*answers.get(position).setRating(String.valueOf(Integer.valueOf(answers.get(position).getRating()) - 1));
                notifyDataSetChanged();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView answer, rating;
        public ImageView up, down;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            answer = itemView.findViewById(R.id.txt_answer);
            rating = itemView.findViewById(R.id.txt_rating);
            up = itemView.findViewById(R.id.btn_up);
            down = itemView.findViewById(R.id.btn_down);

        }
    }

    private void updateRating(final String answerID, final String oldRate, final String status,
                              final String addedBy, final int position){
        HttpsTrustManager.allowAllSSL();
        String url = Constants.API_URL + "answers/editRating/" + answerID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                Log.e("Error", response);

                try {
                    JSONObject result = new JSONObject(response);

                    String updatedRate = result.getString("rating");

                    answers.get(position).setRating(updatedRate);
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error+"", Toast.LENGTH_LONG).show();
                Log.e("Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("rating", oldRate);
                params.put("uid", auth.getCurrentUser().getUid());
                params.put("rateChange", status);
                params.put("addedBy", addedBy);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
