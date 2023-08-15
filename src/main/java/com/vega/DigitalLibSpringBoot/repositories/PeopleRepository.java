package com.vega.DigitalLibSpringBoot.repositories;

import com.vega.DigitalLibSpringBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    List<Person> findByFullName(String fullName);
}
