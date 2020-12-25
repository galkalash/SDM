package Engine;

import Dtos.FeedbackDto;

import java.util.Date;

public class Feedback {
    private final String customerName;
    private final int orderId;
    private final Rating rating;
    private final String feedback;
    private final Date date;

    public Feedback(String customerName, int orderId, Rating rating, String feedback, Date date) {
        this.customerName = customerName;
        this.orderId = orderId;
        this.rating = rating;
        this.feedback = feedback;
        this.date = date;
    }

    public Feedback(FeedbackDto newFeedback){
        this.customerName = newFeedback.getCustomerName();
        this.orderId = newFeedback.getOrderId();
        this.rating = newFeedback.getRating();
        this.feedback = newFeedback.getFeedback();
        this.date = newFeedback.getDate();
    }

    public Rating getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getDate() {
        return date;
    }
}
