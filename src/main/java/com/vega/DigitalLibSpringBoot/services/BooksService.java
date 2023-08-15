package com.vega.DigitalLibSpringBoot.services;

import com.vega.DigitalLibSpringBoot.models.Book;
import com.vega.DigitalLibSpringBoot.models.Person;
import com.vega.DigitalLibSpringBoot.repositories.BooksRepository;
import com.vega.DigitalLibSpringBoot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> indexWithPagination(int page, int booksPerPage, boolean sortByYear){
        if(sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else{
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> index(boolean sortByYear){
        if(sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public Book show (int id){
        return booksRepository.findById(id).orElse(null);
    }
    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }
    public List<Book> booksStartingWith(String prefix){
        return booksRepository.findByTitleStartingWith(prefix);
    }

    public Person getBookOwner(int id){
        return booksRepository.findById(id).map(new Function<Book, Person>() {
            @Override
            public Person apply(Book book) {
                return book.getOwner();
            }
        }).orElse(null);
    }

    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setDateOfReceive(null);
        });
    }

    @Transactional
    public void assign(int id, Person owner){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(owner);
            book.setDateOfReceive(new Date());
        });
    }

}
