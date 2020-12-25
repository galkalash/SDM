package Dtos;

import Engine.Rating;

public class FeedbackAlertDto {
    private final String customerName;
    private final int orderId;
    private final Rating rating;
    private final String feedback;

    public FeedbackAlertDto(FeedbackDto feedback) {
        this.customerName = feedback.getCustomerName();
        this.orderId = feedback.getOrderId();
        this.rating = feedback.getRating();
        this.feedback = feedback.getFeedback();
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public Rating getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }
}
