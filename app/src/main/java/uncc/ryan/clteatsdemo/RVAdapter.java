package uncc.ryan.clteatsdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryrid on 4/4/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RestaurantViewHolder> {
    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            //restDistance.setText((int) restaurant.getDistance_miles());
            //restRating.setText((int) restaurant.getRating());
        }

        public void onClick(View v){

        }
    }

    ArrayList<Restaurant> restaurantArrayList;

    public RVAdapter(SearchActivity searchResultsActivity, ArrayList<Restaurant> restaurants){
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
}
