package kf.csvreader.project.service;

import kf.csvreader.project.helper.CSVHelper;
import kf.csvreader.project.model.Weather;
import kf.csvreader.project.repository.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Service with implementations of methods from repository
 */
@Service
public class WeatherService {

    @Autowired
    WeatherRepo repository;

    /**
     * Saves data in repository
     * @param file given CSV data
     * @throws RuntimeException for failures
     */
    public void save(MultipartFile file) {
        try {
            List<Weather> weathers = CSVHelper.csvDataToWeathers(file.getInputStream());
            repository.saveAll(weathers);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    /**
     * Returns all data from repository
     * @return data from repository
     */
    public List<Weather> getAll() {
        return repository.findAll();
    }

    /**
     * Returns average temperature for given city
     * @param city given city
     * @return average temperature if present or optional
     */
    public OptionalDouble getAverageByCity(String city){
        return repository.findAll().stream()
                .filter(s->s.getCity().contentEquals(city))
                .mapToDouble(Weather::getTemp)
                .average();
    }

}

