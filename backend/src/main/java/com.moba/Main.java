package com.moba;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.moba.Customer.Customer;
import com.moba.Customer.CustomerRepository;
import com.moba.Customer.Gender;
import com.moba.s3.S3Buckets;
import com.moba.s3.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;


@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            S3Service s3Service,
            S3Buckets s3Buckets) {
        return args -> {

            createRandomCustomer(customerRepository, passwordEncoder);
            testBucketUploadAndDownload(s3Service, s3Buckets);
        };
    }

    private static void testBucketUploadAndDownload(S3Service s3Service, S3Buckets s3Buckets) {
        s3Service.putObject(
                s3Buckets.getCustomer(),
                "foo/bar/jamila",
                "Helo World".getBytes());

        byte[] obj = s3Service.getObject("put the exact buket name in aws s3",
                "foo");

        System.out.println("Good to go" + new String(obj));
    }

    private static void createRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        Faker faker = new Faker();
        Random random = new Random();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@moba.com";
        Customer customer = new Customer(
                firstName +" "+ lastName,
                email,
                passwordEncoder.encode("password"), age,
                gender);

        customerRepository.save(customer);
        System.out.println(email);
    }
}
