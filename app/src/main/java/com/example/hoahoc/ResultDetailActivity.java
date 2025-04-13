package com.example.hoahoc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

            if (userAnswer != null) {
                holder.tvUserAnswer.setText("Đáp án bạn chọn: " + userAnswer);
                if (userAnswer.equals(question.getCorrectAnswer())) {
                    holder.tvStatus.setText("Kết quả: Đúng");
                    holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    holder.tvStatus.setText("Kết quả: Sai");
                    holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
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

