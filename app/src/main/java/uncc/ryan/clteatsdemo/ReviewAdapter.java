package uncc.ryan.clteatsdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryan on 4/22/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userComment;

        ReviewViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.tvReviewAdapterUserName);
            userComment = (TextView) itemView.findViewById(R.id.tvReviewAdapterUserComment);
        }

        public void bindReview(Review review) {
            userName.setText(review.getUserName());
            userComment.setText(review.getUserComment());
        }
    }

    ArrayList<Review> reviewArrayList;

    public ReviewAdapter(PopupWindow searchActivity, ArrayList<Review> reviews){
        reviewArrayList = reviews;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item_layout, viewGroup, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(v);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int i){
        holder.bindReview(reviewArrayList.get(i));
    }

    @Override
    public int getItemCount(){
        if(reviewArrayList.size() > 0){
            return reviewArrayList.size();
        }else{
            return 0;
        }
    }
}
