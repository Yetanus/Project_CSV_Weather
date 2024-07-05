package kf.csvreader.project.repository;

import kf.csvreader.project.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Weather repository
 */
@Repository
public interface WeatherRepo extends JpaRepository<Weather, Integer> {
}
