package com.example.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<SalesData, String> {

    @Query("SELECT SUM(s.amount) FROM SalesData s")
    Double getTotalSales();
}