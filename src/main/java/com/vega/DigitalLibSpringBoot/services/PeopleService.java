package com.vega.DigitalLibSpringBoot.services;

import com.vega.DigitalLibSpringBoot.models.Book;
import com.vega.DigitalLibSpringBoot.models.Person;
import com.vega.DigitalLibSpringBoot.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index(){
        return peopleRepository.findAll();
    }

    public Person show(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findPersonByFullName(String fullName){
        return peopleRepository.findByFullName(fullName).stream().findFirst();
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepository.findById(id);

        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long differenceInMS = Math.abs(new Date().getTime() - book.getDateOfReceive().getTime()); // We can use also System.CurrentTimeMillis()

                if(differenceInMS > 864000000){
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        }
        else{
            return Collections.emptyList();
        }
    }
}
