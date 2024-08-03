package lk.ijse.home_practice02.dao;

import lk.ijse.home_practice02.dto.StudentDto;

import java.sql.Connection;
import java.sql.SQLException;

public sealed interface StudentData permits StudentProcess{
    boolean saveStudent(StudentDto studentDto, Connection connection) throws SQLException;
    boolean deleteStudent(String studentId,Connection connection) throws SQLException;
    StudentDto getAll(String studentId,Connection connection) throws SQLException;

    boolean updateStudent(String studentId,StudentDto studentDTO,Connection connection) throws SQLException;
}
