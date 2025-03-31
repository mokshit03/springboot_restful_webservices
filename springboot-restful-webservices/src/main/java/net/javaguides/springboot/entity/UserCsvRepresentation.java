package net.javaguides.springboot.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCsvRepresentation {

    @CsvBindByName(column = "username")
    private String username;

    @CsvBindByName(column = "firstname")
    private String firstname;

    @CsvBindByName(column = "lastname")
    private String lastname;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "roles")
    private String roles;

}
