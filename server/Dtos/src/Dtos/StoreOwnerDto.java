package Dtos;

import Engine.Feedback;
import Engine.Store;
import User.*;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreOwnerDto extends UserDto{

    private HashMap<String,List<StoreDto>> stores;
    private List<FeedbackDto> feedbacks;

    public StoreOwnerDto(StoreOwner storeOwner) {
        super(storeOwner);
        stores = new HashMap<>();
        for(Map.Entry<String,List<Store>> zone : storeOwner.getStores().entrySet()){
            stores.put(zone.getKey() , new ArrayList<>());
            for(Store store : zone.getValue()){
                stores.get(zone.getKey()).add(new StoreDto(store));
            }
        }

        feedbacks = new ArrayList<>();
        for(Feedback feedback : storeOwner.getFeedbacks()){
            feedbacks.add(new FeedbackDto(feedback));
        }
    }

    public HashMap<String, List<StoreDto>> getStores() {
        return stores;
    }

    public List<FeedbackDto> getFeedbacks() {
        return feedbacks;
    }

    public void setStores(HashMap<String, List<StoreDto>> stores) {
        this.stores = stores;
    }

    public void setFeedbacks(List<FeedbackDto> feedbacks) {
        this.feedbacks = feedbacks;
    }


}
