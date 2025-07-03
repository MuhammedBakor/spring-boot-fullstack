package com.moba.Customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        //Given
        CustomerRowMapper mapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("jhon@gmail.com");
        when(resultSet.getString("gender")).thenReturn("FEMALE");

        //When
        Customer actual = mapper.mapRow(resultSet, 1);

        //Then
        Customer expected = new Customer(
                1,
                "John",
                "jhon@gmail.com",
                "password", 19,
                Gender.FEMALE);

        assertThat(actual).isEqualTo(expected);
    }
}