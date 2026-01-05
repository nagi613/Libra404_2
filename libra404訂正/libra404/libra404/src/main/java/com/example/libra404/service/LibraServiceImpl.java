package com.example.libra404.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.libra404.dao.LibraDao;
import com.example.libra404.model.Book;
import com.example.libra404.model.Category;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Kasidasi;
import com.example.libra404.model.Member;

@Service
public class LibraServiceImpl implements LibraService {
    private final LibraDao libraDao;
    LibraServiceImpl(LibraDao libraDao){
        this.libraDao = libraDao;
    }

    @Override
    public int insertMember(Member member){
        return libraDao.insertMember(member);
    }

    @Override
    public Member findByLoginMember(Map<String, String> login_info){
        return libraDao.findByLoginMember(login_info);
    }

    @Override
    public Integer findByMailMember(String mail){
        return libraDao.findByMailMember(mail);
    }

    @Override
    public int updateMember(Member member){
        return libraDao.updateMember(member);
    }

    @Override
    public int insertBook(Book book){
        return libraDao.insertBook(book);
    }

    @Override
    public Book findByIdBook(int id){
        return libraDao.findByIdBook(id);
    }

    @Override
    public List<Book> searchBook(String keyword, int cid){
        return libraDao.searchBook(keyword, cid);
    }

    @Override
    public int updateBook(Book book){
        return libraDao.updateBook(book);
    }

    @Override
    public int lentBook(Kasidasi kasidasi){
        return libraDao.lentBook(kasidasi);
    }

    @Override
    public List<Book> getBookList(){
        return libraDao.getBookList();
    }

    @Override
    public List<Category> getCategoryList(){
        return libraDao.getCategoryList();
    }

    @Override
    public List<Kasidasi> getKasidasiList(){
        return libraDao.getKasidasiList();
    }

    //メソッドの追加
    @Override
    public void updateBookLent(int honid){
        int ret = libraDao.updateBookLent(honid);
    }

    @Override
    public List<DispKasidasi> getShelfBookList(int mid){
        return libraDao.getShelfBookList(mid);
    }
}
