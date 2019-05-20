package com.julu666.course.api.controllers;


import com.julu666.course.api.constants.Global;
import com.julu666.course.api.jpa.*;
import com.julu666.course.api.repositories.ApplyBookRepository;
import com.julu666.course.api.repositories.BooksRepository;
import com.julu666.course.api.repositories.CategoriesRepository;
import com.julu666.course.api.requests.book.ApplyForBookRequest;
import com.julu666.course.api.requests.book.ApprovalBookRequest;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BooksController {

    @Autowired
    private CategoriesRepository categoriesRepository;


    @Resource(name = "booksRepository")
    private BooksRepository booksRepository;

    @Resource(name = "applyBookRepository")
    private ApplyBookRepository applyBookRepository;

    @GetMapping("/categories")
    public Response<List<SampleCategories>> categories(@RequestHeader(value = "Token") String token) {
        Iterable<SampleCategories> sampleCategories = categoriesRepository.findAll();
        List<SampleCategories> list = new ArrayList<>();
        sampleCategories.forEach(list::add);
        return new Response<>(200, "", list);
    }

    @GetMapping("/books")
    public Response<List<Books>> books(@RequestHeader(value = "Token") String token, @RequestParam(value = "categoryId") Long categoryId) {
        List<Books> books = booksRepository.findByCategoryId(categoryId);

        appendUrl(books);

        return new Response<>(200, "", books);
    }

    @PostMapping("/apply")
    public Response<String> applyBooks(@RequestHeader(value = "Token") String token, @RequestBody ApplyForBookRequest applyForBookRequest) {
        String userId = JWTToken.userId(token);

        for (Long bookId : applyForBookRequest.getBookIds()) {
            Optional<ApplyBooks> applyBooks = applyBookRepository.findByUserIdAndBookId(userId, bookId);
            if (applyBooks.isPresent()) {
                Optional<Books> book = booksRepository.findById(bookId);
                return Wrapper.failActionResp("您已经申请过" + book.get().getName() + "这本书了，无法重复申请", "");
            }
            ApplyBooks apply = new ApplyBooks();
            apply.setAdminId(Global.AdminId);
            apply.setBookId(bookId);
            apply.setStatus(0);
            apply.setUserId(userId);
            applyBookRepository.save(apply);
        }
        return Wrapper.okActionResp("申请成功","");
    }

    @GetMapping("/my")
    public Response<List<ApplyBooks>> myBooks(@RequestHeader(value = "Token") String token, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(token);

        Page<ApplyBooks> pages = applyBookRepository.findByUserIdTop10(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));

        List<ApplyBooks> books = pages.getContent();
        for(ApplyBooks ab : books) {
            Books b = ab.getBooks();
            String tkFile = b.getIcon();

            if (tkFile == null) {
                continue;
            }
            String fileDownloadUri = downloadUri(tkFile);
            b.setIcon(fileDownloadUri);
            ab.setBooks(b);
        }

        return new Response<>(200, "", books);
    }

    @PostMapping("/approval")
    public Response<String> approval(@RequestHeader(value = "Token") String token, @RequestBody ApprovalBookRequest approvalBookRequest) {
        if (!applyBookRepository.findById(approvalBookRequest.getId()).isPresent()) {
            return Wrapper.failActionResp("无相关数据", "");
        }
        ApplyBooks applyBooks = applyBookRepository.findById(approvalBookRequest.getId()).get();
        applyBooks.setStatus(approvalBookRequest.getStatus());
        applyBookRepository.save(applyBooks);
        return Wrapper.okActionResp("操作成功","");
    }


    @PostMapping("/receive")
    public Response<String> receive(@RequestHeader(value = "Token") String token, @RequestParam(value = "id") Long id) {
        if (!applyBookRepository.findById(id).isPresent()) {
            return Wrapper.failActionResp("无相关记录", "");
        }
        ApplyBooks applyBooks = applyBookRepository.findById(id).get();
        applyBooks.setStatus(4);
        applyBookRepository.save(applyBooks);
        return Wrapper.okActionResp("签收成功","");

    }

    private String downloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https")
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
    }

    private void appendUrl(List<Books> pageCourse) {
        for (Books book : pageCourse) {
            String tkFile = book.getIcon();

            if (tkFile == null) {
                continue;
            }
            String fileDownloadUri = downloadUri(tkFile);
            book.setIcon(fileDownloadUri);
        }
    }



}
