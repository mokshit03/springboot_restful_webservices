package net.javaguides.springboot.service.impl;
 
import com.opencsv.bean.CsvToBeanBuilder;
import net.javaguides.springboot.entity.UserCsvRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
 
@Service
public class CsvParserService {
    
    public List<UserCsvRepresentation> parseCsvFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return new CsvToBeanBuilder<UserCsvRepresentation>(reader)
                    .withType(UserCsvRepresentation.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        }
    }
}