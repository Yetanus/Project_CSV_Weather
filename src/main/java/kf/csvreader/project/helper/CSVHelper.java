package kf.csvreader.project.helper;

import kf.csvreader.project.model.Weather;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class, which opens CSV file, reads data and parses it correctly to Weather model.
 *
 */
@Component
public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERsCSV = { "City","Date","Temp" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    /**
     * Method which reads and process data.
     * @param is Input stream
     * @throws IOException for failures when processing file.
     * @return List with parsed data
     */
    public static List<Weather> csvDataToWeathers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){

            List<Weather> weathers = new ArrayList<>(15000);

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Random random = new Random();
                Weather weather = new Weather(
                        Integer.parseInt(String.valueOf(random.nextInt( 1000000))),
                        csvRecord.get(0),
                        csvRecord.get(1),
                        Double.parseDouble(csvRecord.get(2))
                );
                weathers.add(weather);
            }
            return weathers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
