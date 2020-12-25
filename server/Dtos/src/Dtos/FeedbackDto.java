package Dtos;

import Engine.Feedback;
import Engine.Rating;

import java.util.Date;
import java.util.PrimitiveIterator;

public class FeedbackDto {
    private final String customerName;
    private final int orderId;
    private final Rating rating;
    private final String feedback;
    private final Date date;

    public FeedbackDto(Feedback newFeedback) {
        this.customerName = newFeedback.getCustomerName();
        this.orderId = newFeedback.getOrderId();
        this.rating = newFeedback.getRating();
        this.feedback = newFeedback.getFeedback();
        this.date = newFeedback.getDate();
    }

    public FeedbackDto(String customerName, int orderId, Rating rating, String feedback, Date date) {
        this.customerName = customerName;
        this.orderId = orderId;
        this.rating = rating;
        this.feedback = feedback;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Feedback" +
                "customerName: '" + customerName +
                ", orderId: " + orderId +
                ", rating: " + rating +
                ", feedback: '" + feedback;
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

    public Date getDate() {
        return date;
    }

    public int getOrderId() {
        return orderId;
    }
}
