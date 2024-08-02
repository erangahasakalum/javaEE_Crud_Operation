package lk.ijse.home_practice02.dao;

import lk.ijse.home_practice02.dto.StudentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class StudentProcess implements StudentData {

    StudentDto studentDto = new StudentDto();
    static String SAVE_STUDENT ="INSERT INTO student(id,name,city,telephone) VALUES(?,?,?,?)";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id=?";
    static String GET_STUDENT = "SELECT * FROM student WHERE id=?";

    static String UPDATE_STUDENT = "UPDATE student SET name = ? ,city = ? ,telephone = ?  WHERE id = ?";

    @Override
    public boolean saveStudent(StudentDto studentDto, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SAVE_STUDENT);
        ps.setString(1,studentDto.getId());
        ps.setString(2,studentDto.getName());
        ps.setString(3,studentDto.getCity());
        ps.setString(4,studentDto.getTelephone());

        return ps.executeUpdate()>0;
    }

    @Override
    public boolean deleteStudent(String studentId, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_STUDENT);
        ps.setString(1,studentId);
        return ps.executeUpdate()>0;
    }

    @Override
    public StudentDto getAll(String studentId, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_STUDENT);
        ps.setString(1,studentId);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()){
            studentDto.setId(resultSet.getString("id"));
            studentDto.setName(resultSet.getString("name"));
            studentDto.setCity(resultSet.getString("city"));
            studentDto.setTelephone(resultSet.getString("telephone"));
        }

        return studentDto;
    }

    @Override
    public boolean update(String studentId, StudentDto studentDto, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(UPDATE_STUDENT);
        ps.setString(1,studentDto.getName());
        ps.setString(2,studentDto.getCity());
        ps.setString(3,studentDto.getTelephone());
        ps.setString(4,studentDto.getId());

        return ps.executeUpdate() > 0 ;
    }
}
