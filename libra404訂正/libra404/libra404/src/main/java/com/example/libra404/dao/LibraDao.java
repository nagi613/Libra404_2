package com.example.libra404.dao;

import java.util.List;
import java.util.Map;

import com.example.libra404.model.Book;
import com.example.libra404.model.Category;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Kasidasi;
import com.example.libra404.model.Member;

public interface LibraDao {
    int insertMember(Member member);
    Member findByLoginMember(Map<String, String> login_info);
    Integer findByMailMember(String mail);
    int updateMember(Member member);
    int insertBook(Book book);
    Book findByIdBook(int id);
    List<Book> searchBook(String word, int cid);
    int updateBook(Book book);
    int lentBook(Kasidasi kasidasi);
    List<Book> getBookList();
    String findByIdCategory(int cid);
    String findByIdWriter(int wid);
    String findByIdPublisher(int puid);
    List<Category> getCategoryList();
    List<Kasidasi> getKasidasiList();

    //メソッドの追加
    int updateBookLent(int honid);
    List<DispKasidasi> getShelfBookList(int mid);

}