package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User user1=new User();
        user1.setName(user.getName());
        user1.setAge(user.getAge());
        user1.setSubscription(user.getSubscription());
        user1.setMobNo(user.getMobNo());
        userRepository.save(user1);
        return user1.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        Optional<User> user=userRepository.findById(userId);
        if(!user.isPresent()) return null;
        int age=user.get().getAge();
        SubscriptionType subscriptionType=user.get().getSubscription().getSubscriptionType();
        List<WebSeries> webSeriesList=webSeriesRepository.findAll();
        int count=0;
        if(subscriptionType.compareTo(SubscriptionType.ELITE)==0) return webSeriesList.size();
        for(WebSeries webSeries :webSeriesList){
            SubscriptionType subscriptionType1=webSeries.getSubscriptionType();
            if(subscriptionType.compareTo(subscriptionType1)==0) count++;
            else if(subscriptionType.compareTo(SubscriptionType.PRO)==0 && subscriptionType1.compareTo(SubscriptionType.BASIC)==0){
                count++;
            }
        }

        return count;
    }


}
