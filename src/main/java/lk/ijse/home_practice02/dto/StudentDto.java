package lk.ijse.home_practice02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class StudentDto implements Serializable {
    private String id;
    private String name;
    private String city;
    private String telephone;
}
