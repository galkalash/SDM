package User;

import Dtos.FeedbackDto;
import Engine.Feedback;
import Engine.Order;
import Engine.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreOwner extends User{

    private HashMap<String,List<Store>> stores;
    private List<Feedback> feedbacks;

    public StoreOwner(String name) {
        super(name);
        this.stores = new HashMap<>();
        this.feedbacks = new ArrayList<>();
    }

    public HashMap<String, List<Store>> getStores() {
        return stores;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setStores(HashMap<String, List<Store>> stores) {
        this.stores = stores;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void addNewStore(String zoneName,Store store){
        if(!stores.containsKey(zoneName)) {
            stores.put(zoneName, new ArrayList<>());
        }
        stores.get(zoneName).add(store);
    }

    public void addOrder(Order newOrder) {
        orders.add(newOrder);
    }

    public void addFeedbackToUser(FeedbackDto newFeedback){
        this.feedbacks.add(new Feedback(newFeedback));
    }
}
