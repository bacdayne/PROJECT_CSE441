package com.example.hoahoc.view.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.R;
import com.example.hoahoc.model.Question;

import java.util.List;

public class ResultDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewResults;
    private ResultAdapter resultAdapter;
    private List<Question> questionList;
    private List<String> userAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));

        questionList = (List<Question>) getIntent().getSerializableExtra("QUESTION_LIST");
        userAnswers = getIntent().getStringArrayListExtra("USER_ANSWERS");

        resultAdapter = new ResultAdapter(questionList, userAnswers);
        recyclerViewResults.setAdapter(resultAdapter);
    }

    static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

        private List<Question> questionList;
        private List<String> userAnswers;

        public ResultAdapter(List<Question> questionList, List<String> userAnswers) {
            this.questionList = questionList;
            this.userAnswers = userAnswers;
        }

        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
            return new ResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ResultViewHolder holder, int position) {
            Question question = questionList.get(position);
            String userAnswer = userAnswers.get(position);

            holder.tvQuestion.setText("Câu " + (position + 1) + ": " + question.getQuestionText());
            holder.tvOptionA.setText("A. " + question.getOptionA());
            holder.tvOptionB.setText("B. " + question.getOptionB());
            holder.tvOptionC.setText("C. " + question.getOptionC());
            holder.tvOptionD.setText("D. " + question.getOptionD());
            holder.tvCorrectAnswer.setText("Đáp án đúng: " + question.getCorrectAnswer());

            // Đặt màu mặc định cho tất cả đáp án (xám nhạt)
            holder.tvOptionA.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tvOptionB.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tvOptionC.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tvOptionD.setBackgroundColor(Color.parseColor("#E0E0E0"));

            // Đánh dấu đáp án đúng (màu xanh nhạt)
            String correctAnswer = question.getCorrectAnswer();
            boolean isValidCorrectAnswer = correctAnswer != null && (correctAnswer.equals("A") || correctAnswer.equals("B") || correctAnswer.equals("C") || correctAnswer.equals("D"));
            if (isValidCorrectAnswer) {
                switch (correctAnswer) {
                    case "A":
                        holder.tvOptionA.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                        break;
                    case "B":
                        holder.tvOptionB.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                        break;
                    case "C":
                        holder.tvOptionC.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                        break;
                    case "D":
                        holder.tvOptionD.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                        break;
                }
            } else {
                holder.tvCorrectAnswer.setText("Đáp án đúng: Không xác định");
            }

            // Đánh dấu đáp án người dùng chọn
            boolean isValidAnswer = userAnswer != null && (userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C") || userAnswer.equals("D"));
            if (isValidAnswer) {
                holder.tvUserAnswer.setText("Đáp án bạn chọn: " + userAnswer);
                if (isValidCorrectAnswer && userAnswer.equals(correctAnswer)) {
                    // Đáp án đúng
                    holder.tvStatus.setText("Kết quả: Đúng");
                    holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                    switch (userAnswer) {
                        case "A":
                            holder.tvOptionA.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                            break;
                        case "B":
                            holder.tvOptionB.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                            break;
                        case "C":
                            holder.tvOptionC.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                            break;
                        case "D":
                            holder.tvOptionD.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                            break;
                    }
                } else {
                    // Đáp án sai
                    holder.tvStatus.setText("Kết quả: Sai");
                    holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                    switch (userAnswer) {
                        case "A":
                            holder.tvOptionA.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case "B":
                            holder.tvOptionB.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case "C":
                            holder.tvOptionC.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case "D":
                            holder.tvOptionD.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                            break;
                    }
                }
            } else {
                holder.tvUserAnswer.setText("Bạn chưa trả lời câu này");
                holder.tvStatus.setText("Kết quả: Chưa làm");
                holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
            }
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

        static class ResultViewHolder extends RecyclerView.ViewHolder {
            TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvUserAnswer, tvCorrectAnswer, tvStatus;

            public ResultViewHolder(View itemView) {
                super(itemView);
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                tvOptionA = itemView.findViewById(R.id.tvOptionA);
                tvOptionB = itemView.findViewById(R.id.tvOptionB);
                tvOptionC = itemView.findViewById(R.id.tvOptionC);
                tvOptionD = itemView.findViewById(R.id.tvOptionD);
                tvUserAnswer = itemView.findViewById(R.id.tvUserAnswer);
                tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }
        }
    }
}