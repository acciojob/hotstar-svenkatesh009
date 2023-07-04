package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        int userId=subscriptionEntryDto.getUserId();
        Optional<User> user =userRepository.findById(userId);
        if(!user.isPresent()) return null;
        int amount=0;
        SubscriptionType subscriptionType=subscriptionEntryDto.getSubscriptionType();

        if(subscriptionType.compareTo(SubscriptionType.BASIC)==0) amount=500+200*subscriptionEntryDto.getNoOfScreensRequired();
        else if(subscriptionType.compareTo(SubscriptionType.PRO)==0) amount=800+250*subscriptionEntryDto.getNoOfScreensRequired();
        else amount=1000+350*subscriptionEntryDto.getNoOfScreensRequired();
        subscription.setTotalAmountPaid(amount);
        user.get().setSubscription(subscription);
        userRepository.save(user.get());
        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> user=userRepository.findById(userId);
        if(!user.isPresent()) return null;
        SubscriptionType subscriptionType=user.get().getSubscription().getSubscriptionType();
        if(subscriptionType.compareTo(SubscriptionType.ELITE)==0) throw new Exception("Already the best Subscription");
        int amountAlreadyPaid=user.get().getSubscription().getTotalAmountPaid();
        int noOfScrrens=user.get().getSubscription().getNoOfScreensSubscribed();
        int amountNeedToPay=1000+350*noOfScrrens;
        user.get().getSubscription().setSubscriptionType(SubscriptionType.ELITE);
        user.get().getSubscription().setTotalAmountPaid(amountNeedToPay);
        userRepository.save(user.get());
        return amountNeedToPay-amountAlreadyPaid;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList=subscriptionRepository.findAll();
        int amount=0;
        for(Subscription subscription: subscriptionList ){
            amount+=subscription.getTotalAmountPaid();
        }
        return amount;
    }

}
