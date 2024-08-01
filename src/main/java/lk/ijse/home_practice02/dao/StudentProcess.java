package lk.ijse.home_practice02.dao;

import lk.ijse.home_practice02.dto.StudentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class StudentProcess implements StudentData {
    static String SAVE_STUDENT ="INSERT INTO student(id,name,city,telephone) VALUES(?,?,?,?)";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id=?";
    static String GET_STUDENT = "SELECT * FROM student WHERE id=?";

    static String UPDATE_STUDENT = "UPDATE student SET name = ? ,city = ? ,telephone = ? , WHERE id = ?";

    @Override
    public boolean saveStudent(StudentDto studentDto, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SAVE_STUDENT);
        ps.setString(1,studentDto.getId());
        ps.setString(2,studentDto.getName());
        ps.setString(3,studentDto.getCity());
        ps.setString(4,studentDto.getTelephone());

        return ps.executeUpdate()>0;
    }
}
