package com.example.runningcoach_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<FeedbackResult> feedbackResults;

    public CustomAdapter(ArrayList<FeedbackResult> feedbackResults) {
        this.feedbackResults = feedbackResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedbackResult feedbackResult = feedbackResults.get(position);

        // TODO: ViewHolder에 feedbackResult의 데이터를 설정하세요.

        // 예시:
        holder.numTxt.setText(String.valueOf(position + 1));
        holder.dateTxt.setText(feedbackResult.getResultDate());
        holder.upTxt2.setText(String.valueOf(feedbackResult.getResultUpperBodyAngle()));
        holder.upfeedbackTxt.setText(feedbackResult.getResultUpperBodyAngleTxt());
        holder.legTxt2.setText(String.valueOf(feedbackResult.getResultLegAngle()));
        holder.legfeedbackTxt.setText(feedbackResult.getResultLegAngleTxt());
    }

    @Override
    public int getItemCount() {
        return feedbackResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numTxt, dateTxt, upTxt1, upTxt2, upfeedbackTxt, legTxt1, legTxt2, legfeedbackTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numTxt = itemView.findViewById(R.id.numTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            upTxt1 = itemView.findViewById(R.id.upTxt1);
            upTxt2 = itemView.findViewById(R.id.upTxt2);
            upfeedbackTxt = itemView.findViewById(R.id.upfeedbackTxt);
            legTxt1 = itemView.findViewById(R.id.legTxt1);
            legTxt2 = itemView.findViewById(R.id.legTxt2);
            legfeedbackTxt = itemView.findViewById(R.id.legfeedbackTxt);
        }
    }
}
