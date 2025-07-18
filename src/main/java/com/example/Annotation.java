package com.example;

// I prefer long imports to importing everything at once lombok.*
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public class Annotation {
    public static void main(String... args) {
        // Data
        System.out.println("=== Person (@Data) ===");
        Person person = new Person();
        person.setName("John Doe");
        person.setAge(25);
        person.setEmail("person@email.com");
        System.out.println(person); // Human-readable inf with generated toString()

        // RequiredArgsConstructor
        System.out.println("\n=== Car (@RequiredArgsConstructor) ===");
        Car car = new Car("Буханка", Brand.UAZ, "VIN123");
        System.out.println(car); // Without human-readable info

        // AllArgsConstructor
        System.out.println("\n=== Book (@AllArgsConstructor) ===");
        Book book = new Book("Колобок", "Неизвестен", 123);
        System.out.println(book); // Without human-readable info

        // Builder
        System.out.println("\n=== House (@Builder) ===");
        House house = House.builder()
                .address("Privet drive, 123")
                .rooms(3)
                .area(120)
                .price(250000)
                .build();
        System.out.println(house); // Uses @ToString

        // Getter/Setter
        System.out.println("\n=== Product (@Getter/@Setter) ===");
        Product product = new Product();
        product.setName("Cat food");
        product.setPrice(9999);
        // product.setArticle("ART123"); // Compilation error: no setter
        System.out.println("Name: " + product.getName());
        // System.out.println("Price: " + product.getPrice()); // Compilation error: no getter
        System.out.println("Article: " + product.getArticle()); // null

        // SneakyThrows
        System.out.println("\n=== ErrorThrower (@SneakyThrows) ===");
        ErrorThrower errorThrower = new ErrorThrower();
        try {
            errorThrower.throwException();
        } catch (RuntimeException e) {
            System.out.println("Ignored exception: " + e.getMessage());
        } catch (Throwable t) {
            System.out.println("Caught unexpected throwable: " + t.getMessage());
        }

        // Accessors(chain = true)
        System.out.println("\n=== Configuration (@Accessors(chain = true)) ===");
        Configuration config = new Configuration()
                .setHost("localhost")
                .setPort(8080)
                .setUsername("admin")
                .setPassword("secret");
        System.out.println(config);
        System.out.println("Host: " + config.getHost());
    }
}

// Swiss knife.
// @Getter, @Setter (for non-final fields), @RequiredArgsConstructor (final, NonNull,
// @ToString (from all class's fields), @EqualsAndHashCode (equals(), hashCode() based on class's fields)
@Data
class Person {
    private String name;
    private int age;
    private String email;
}

// Generate the class constructor with required args (final, NonNull) only
@RequiredArgsConstructor
class Car {
    private final String model;
    private final Brand brand;
    @NonNull private String vin;
    private String plateNumber;
}

// Just to try Java's enums
@AllArgsConstructor
enum Brand {
    UAZ("uaz"),
    BMW("bmw"),
    VW("vw");

    @Getter
    private final String value;
}

// Also saves a lot of time generating the class constructor with all args like 'public Book(String title, String author, int pages) { ... }'
@AllArgsConstructor
class Book {
    private String title;
    private String author;
    private int pages;
}

// Saves you from writing tons of code for a class constructor. Replaces 'public House()' with 'House.builder().address("st.")...'
@Builder
class House {
    private String address;
    private int rooms;
    private int area;
    private int price;
}


// Generate boring methods like getName(), setName(), etc
class Product {
    @Getter @Setter
    private String name;

    @Getter
    private String article;

    @Setter
    private int price;
}

// For those lazy programmers who don't want to explicitly return an error.
// I guess they don't like golang-style code like 'func Foo() (int, error) {}'
class ErrorThrower {
    @SneakyThrows
    public void throwException() {
        throw new Exception("Something went wrong (i mean right in this case)");
    }
}

// Generates JS-style chain code for getters or setters. Some people think it's nice and handy.
// See the example in Annotation class
@Getter
@Setter
@Accessors(chain = true)
class Configuration {
    private String host;
    private int port;
    private String username;
    private String password;
}
