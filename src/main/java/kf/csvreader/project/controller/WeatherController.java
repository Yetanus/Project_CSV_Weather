package kf.csvreader.project.controller;

import kf.csvreader.project.helper.CSVHelper;
import kf.csvreader.project.model.Weather;
import kf.csvreader.project.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Controller with methods to process data
 */
@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api/csv")
public class WeatherController {

        @Autowired
        WeatherService fileService;

    /**
     * Method to upload data in CVS format
     * @param file CSV File
     * @return Status OK for correct response, exception if not successfully processed or BAD REQUEST if csv file not provided.
     */
    @PostMapping("/upload")
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
            String message = "";

            if (CSVHelper.hasCSVFormat(file)) {
                fileService.save(file);

                try {
                    fileService.save(file);
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.OK).body( "\" message \": \" "+ message +" \"");
                } catch (Exception e) {
                    message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("\" message \": \" "+ message +" \"");
                }
            }
            message = "Please upload a csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\" message \": \" "+ message +" \"");
        }

    /**
     * Returns list of all given data
     * @return given data
     */
    @GetMapping("/weathers")
    public ResponseEntity<List<Weather>> getAllWeathers() {
        try {
            List<Weather> weathers = fileService.getAll();

            if (weathers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<Weather>>(weathers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns average temperature for a given city
     * @param city given city to check temperature
     * @return returns average temperature if present, NO_Content if not present, or INTERNAL_SERVER_ERROR for other failures.
     */
    @GetMapping("/average/{city}")
    public ResponseEntity<OptionalDouble> getAverageForGivenCity(@PathVariable String city){
        try {
            OptionalDouble weatherAverage = fileService.getAverageByCity(city);

            if (!weatherAverage.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(weatherAverage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
