package motor.mechanic.finder.fyp.DataModels;

public class FeedbackModel {

    private String myname, email, comment, rating, date;

    public FeedbackModel() {
    }

    public FeedbackModel(String myname, String email, String comment, String rating, String date) {
        this.myname = myname;
        this.email = email;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
