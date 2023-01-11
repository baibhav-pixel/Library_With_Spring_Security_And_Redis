package com.example.library.Controllers;

import com.example.library.Models.Book;
import com.example.library.Requests.BookCreateRequest;
import com.example.library.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    //Admin should create book only -- Secure this API
    //BOOK_CREATION_AUTHORITY
    @PostMapping("/book")
    public Book createBook(@Valid @RequestBody BookCreateRequest bookCreateRequest)
    {
        return bookService.createBook(bookCreateRequest);
    }

    //Student and Admin can access this -- Secured endpoint

}
