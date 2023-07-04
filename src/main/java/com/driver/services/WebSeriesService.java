package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        WebSeries webSeries=new WebSeries();
        Optional<WebSeries> webSeries1=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(!webSeries1.isPresent()) throw new Exception("Series is already present");

        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        Optional<ProductionHouse> productionHouse=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if(!productionHouse.isPresent()) return null;
        List<WebSeries> webSeriesList=productionHouse.get().getWebSeriesList();
        webSeriesList.add(webSeries);
        double rating=0;
        for(WebSeries webSeries2 :webSeriesList){
            rating+=webSeries2.getRating();
        }
        productionHouse.get().setRatings(rating/webSeriesList.size());
        productionHouseRepository.save(productionHouse.get());
        return webSeries.getId();
    }

}
