package uncc.ryan.clteatsdemo;

/**
 * Created by Ryan on 4/19/2017.
 */

public class Review {
    String userComment, userName, place_id;
    int userRating;

    @Override
    public String toString() {
        return "" + userName + "\n" +
                "" + userComment +
                '}';
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
