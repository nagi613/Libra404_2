package com.example.libra404.service;

import java.util.List;
import java.util.Map;

import com.example.libra404.model.Book;
import com.example.libra404.model.Category;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Kasidasi;
import com.example.libra404.model.Member;

public interface LibraService {
    int insertMember(Member member);
    Member findByLoginMember(Map<String, String> login_info);
    Integer findByMailMember(String map);
    int updateMember(Member member);
    int insertBook(Book book);
    Book findByIdBook(int id);
    List<Book> searchBook(String keyword, int cid);
    int updateBook(Book book);
    int lentBook(Kasidasi kasidasi);
    List<Book> getBookList();
    List<Category> getCategoryList();
    List<Kasidasi> getKasidasiList();

    //メソッドの追加
    void updateBookLent(int honid);
    List<DispKasidasi> getShelfBookList(int mid);

}
