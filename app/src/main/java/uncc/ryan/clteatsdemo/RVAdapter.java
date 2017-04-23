package uncc.ryan.clteatsdemo;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryrid on 4/4/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RestaurantViewHolder> {

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView restName;
        TextView restAddress;
        TextView restDistance;
        TextView restRating;

        RestaurantViewHolder(View itemView){
            super(itemView);
            restName = (TextView)itemView.findViewById(R.id.rllName);
            restAddress = (TextView)itemView.findViewById(R.id.rllAddress);
            restDistance = (TextView)itemView.findViewById(R.id.rllDistance);
            restRating = (TextView)itemView.findViewById(R.id.rllRating);
            itemView.setOnClickListener(this);
        }
        public void bindRestaurant(Restaurant restaurant){
            restName.setText(restaurant.getName());
            restAddress.setText(restaurant.getAddress());
            Double mDist = restaurant.getDistance_miles();
            mDist = round(mDist,2);
            restDistance.setText(mDist.toString());
            Double mRating = restaurant.getRating();
            restRating.setText(mRating.toString());
        }

        public void onClick(View v){
            //Log.d("Object onClick",getPosition()+"");
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    ArrayList<Restaurant> restaurantArrayList;

    public RVAdapter(Activity searchResultsActivity, ArrayList<Restaurant> restaurants){
        restaurantArrayList = restaurants;
    }

    @Override
    public RVAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_result_object, viewGroup, false);
        RestaurantViewHolder restaurantViewHolder = new RestaurantViewHolder(v);
        return restaurantViewHolder;
    }

    @Override
    public void onBindViewHolder(RVAdapter.RestaurantViewHolder holder, int i) {
        holder.bindRestaurant(restaurantArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        if(restaurantArrayList.size() > 0){
            return restaurantArrayList.size();
        }else{
            return 0;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
