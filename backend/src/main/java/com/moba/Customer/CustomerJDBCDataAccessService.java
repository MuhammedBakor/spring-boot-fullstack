package com.moba.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        var sql = """
                SELECT id, name, email, age FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);

    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, name, email, age 
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (Name, email,age)
                VALUES (?, ?, ?)
                """;
        int update = jdbcTemplate.update(
                sql,
                customer.getName(), customer.getEmail(), customer.getAge());

        System.out.println("The Update is:......... "+update);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {

        var sql = """
                SELECT Count(id) 
                FROM customer
                WHERE email = ?
                """;

            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count> 0;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var sql = """
                SELECT Count(id) 
                FROM customer
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count> 0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE 
                FROM customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, customerId);

        System.out.println("The Delete is:......... " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId());
            System.out.println("The Updated name is:......... " + result);
        }

        if (customer.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("The Updated age is:......... " + result);
        }

        if(customer.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );

            System.out.println("The Updated email is:......... " + result);
        }
    }
}
