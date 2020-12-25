package User;

import Dtos.CustomerDto;
import Dtos.FeedbackDto;
import Dtos.StoreOwnerDto;
import Dtos.UserDto;
import Engine.Store;

import java.util.*;

public class UserManager {

    private final Map<String,User> marketUsers;

    public UserManager(){
        this.marketUsers = new HashMap<>();
    }

    public synchronized void addUser(String UserName, UserType userType){
        if(userType == UserType.shopOwner){
            marketUsers.put(UserName, new StoreOwner(UserName));
        }
        else{
            marketUsers.put(UserName, new Customer(UserName));
        }

    }

    public synchronized boolean isUserExist(String UserName){
        return marketUsers.containsKey(UserName);
    }

    public UserDto getUserByName(String name){
        if(!marketUsers.containsKey(name)){
            throw new IllegalArgumentException("ERROR. " + name + " is not exist in the market.");
        }
        if(marketUsers.get(name) instanceof Customer){
            return new CustomerDto((Customer) marketUsers.get(name));
        }
        else{
            return new StoreOwnerDto((StoreOwner)marketUsers.get(name));
        }
    }

    public List<UserDto> getAllUsers(){
        List<UserDto> users = new ArrayList<>();
        for(Map.Entry<String , User> user : marketUsers.entrySet()){
            if(user.getValue() instanceof Customer){
                users.add(new CustomerDto((Customer)user.getValue()));
            }
            else{
                users.add(new StoreOwnerDto((StoreOwner)user.getValue()));
            }
        }
        return users;
    }

    public Map<String, User> getMarketUsers() {
        return marketUsers;
    }

    public void addMoneyToUser(String username, float amount, Date date){
        marketUsers.get(username).addMoney(amount,date);
    }

    public void addFeedbackTOStoreOwner(String username, FeedbackDto feedback){
        StoreOwner storeOwner = ((StoreOwner)marketUsers.get(username));
        storeOwner.addFeedbackToUser(feedback);
    }

    public float getUserBalance(String username){
        return marketUsers.get(username).getCreditBalance();
    }

    public void addStoreToStoreOwner(Store store , String storeOwnerName, String zoneName){
        StoreOwner storeOwner = (StoreOwner)marketUsers.get(storeOwnerName);
        storeOwner.addNewStore(zoneName,store);
    }
}
