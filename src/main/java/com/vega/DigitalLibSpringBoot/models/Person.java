package com.vega.DigitalLibSpringBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "People")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Incorrect format of Name")
    @Column(name = "full_name")
    private String fullName;
    @Min(value = 0, message = "Incorrect!")
    @Column(name = "year_of_the_birth")
    private int yearOfTheBirth;
    @Email
    @NotEmpty(message = "Email cant be empty!")
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(int id, String fullName, int yearOfTheBirth, String email) {
        this.id = id;
        this.fullName = fullName;
        this.yearOfTheBirth = yearOfTheBirth;
        this.email = email;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String full_name) {
        this.fullName = full_name;
    }

    public int getYearOfTheBirth() {
        return yearOfTheBirth;
    }

    public void setYearOfTheBirth(int yearOfTheBirth) {
        this.yearOfTheBirth = yearOfTheBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
