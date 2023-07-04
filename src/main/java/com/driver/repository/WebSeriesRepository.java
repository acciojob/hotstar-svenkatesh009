package com.driver.repository;

import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    Optional<WebSeries> findBySeriesName(String seriesName);
}
