package com.vega.DigitalLibSpringBoot.controllers;

import com.vega.DigitalLibSpringBoot.models.Book;
import com.vega.DigitalLibSpringBoot.models.Person;
import com.vega.DigitalLibSpringBoot.services.BooksService;
import com.vega.DigitalLibSpringBoot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year") Boolean sortByYear){
        if(page == null || booksPerPage == null){
            model.addAttribute("books", booksService.index(sortByYear));
        }else{
            model.addAttribute("books", booksService.indexWithPagination(page, booksPerPage, sortByYear));
        }

        return "/books/index";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String doSearch(Model model, @RequestParam("prefix") String prefix){
        model.addAttribute("books", booksService.booksStartingWith(prefix));

        return "books/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.show(id));
        Person bookOwner = booksService.getBookOwner(id);
        if(bookOwner != null){
            model.addAttribute("reserve", bookOwner);
        } else{
            model.addAttribute("people", peopleService.index());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "/books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id){
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }
}
