package com.example.libra404.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.libra404.model.Book;
import com.example.libra404.model.Category;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Kasidasi;
import com.example.libra404.model.Member;

@Repository
public class LibraDaoImpl implements LibraDao{
    private final JdbcTemplate jdbcTemplate;

    LibraDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertMember(Member member){
        String sql = "INSERT INTO member(mname,mail,password,address) VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql, member.getMname(), member.getMail(),member.getPassword(),member.getAddress());
    }

    @Override
    public Member findByLoginMember(Map<String, String> login_info){
        String sql = "SELECT * FROM member WHERE mail = ? AND password = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, login_info.get("mail"), login_info.get("password"));
        Member member = new Member();
        member.setMid((int)result.get("mid"));
        member.setMname((String)result.get("mName"));
        member.setMail((String)result.get("mail"));
        member.setPassword((String)result.get("password"));
        member.setAddress((String)result.get("address"));
        return member;
    }

    @Override
    public Integer findByMailMember(String mail){
        String sql = "SELECT mid FROM member WHERE mail = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, mail);
        return (Integer)result.get("mid");
    }

    @Override
    public int updateMember(Member member){
        String sql = "UPDATE member SET mName = ?, mail = ?, password = ?, address = ? WHERE mid = ?";
        return jdbcTemplate.update(sql, member.getMname(), member.getMail(), member.getPassword(), member.getAddress(), member.getMid());
    }

    @Override
    public int insertBook(Book book){
        String sql = "INSERT INTO booktb(title,cid,wid,puid,synopsis,photo) VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, book.getTitle(), book.getCid(),book.getWid(),book.getPuid(),book.getSynopsis(),book.getPhoto());
    }

    @Override
    public Book findByIdBook(int id){   
        // sql文を変更（著者・出版社・カテゴリを取り出せるように変更します
        // String sql = "SELECT * FROM booktb WHERE honid = ?";;
        String sql = "SELECT * ,wname,puname,category FROM booktb b join writertb w on b.wid=w.wid "
                        + "join putb p on b.puid=p.puid "
                        + "join ctb c on b.cid=c.cid WHERE honid = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);
        Book book = new Book();
        book.setHonid((int)result.get("honid"));
        book.setTitle((String)result.get("title"));
        book.setCid((int)result.get("cid"));
        book.setWid((int)result.get("wid"));
        book.setPuid((int)result.get("puid"));
        book.setSynopsis((String)result.get("synopsis"));
        book.setPhoto((String)result.get("photo"));
        // 貸出状況以下を追加
        book.setBotype((boolean)result.get("botype"));
        book.setWname((String)result.get("wname"));
        book.setPuname((String)result.get("puname"));
        book.setCategory((String)result.get("category"));
        return book;
    }

    @Override
    public List<Book> searchBook(String keyword, int cid){
        List<Map<String, Object>> result;
        if (cid <= 0){
            String sql = "SELECT * FROM booktb WHERE title LIKE ?";
            result = jdbcTemplate.queryForList(sql, "%"+keyword+"%");
        }else {
            String sql = "SELECT * FROM booktb WHERE cid = ? AND title LIKE ?";
            result = jdbcTemplate.queryForList(sql, cid, "%"+keyword+"%");
        }
        List<Book> meetsBooks = new ArrayList<>();
        for (Map<String, Object> res : result){
            Book book = new Book();
            book.setHonid((int)res.get("honid"));
            book.setTitle((String)res.get("title"));
            book.setCid((int)res.get("cid"));
            book.setWid((int)res.get("wid"));
            book.setPuid((int)res.get("puid"));
            book.setSynopsis((String)res.get("synopsis"));
            book.setPhoto((String)res.get("photo"));
            meetsBooks.add(book);
        }
        return meetsBooks;
    }

    @Override
    public int updateBook(Book book){
        String sql = "UPDATE booktb SET title = ?, cid = ?, wid = ?, puid = ?, synopsis = ?, photo = ? WHERE honid = ?";
        return jdbcTemplate.update(sql, book.getTitle(), book.getCid(), book.getWid(), book.getPuid(), book.getSynopsis(), book.getPhoto(), book.getHonid());
    }

    @Override
    public int lentBook(Kasidasi kasidasi){
        // テーブル名にミスあり
        String sql = "INSERT INTO kasitb(boday, mid, honid) VALUES(?,?,?)";
        return jdbcTemplate.update(sql, kasidasi.getBoday(), kasidasi.getMid(), kasidasi.getHonid());
    }

    @Override
    public List<Book> getBookList(){
        String sql = "SELECT * FROM booktb";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        List<Book> bList = new ArrayList<>();
        for(Map<String, Object> result : resultList){
            Book book = new Book();
            book.setHonid((int)result.get("honid"));
            book.setTitle((String)result.get("title"));
            book.setCid((int)result.get("cid"));
            book.setWid((int)result.get("wid"));
            book.setPuid((int)result.get("puid"));
            book.setSynopsis((String)result.get("synopsis"));
            book.setPhoto((String)result.get("photo"));
            // 貸出状況を追加
            book.setBotype((boolean)result.get("botype"));

            bList.add(book);
        }
        return bList;
    }

    @Override
    public String findByIdCategory(int cid){
        String sql = "SELECT category FROM ctb WHERE cid = ?";
        return jdbcTemplate.queryForObject(sql, String.class, cid);
    }

    @Override
    public String findByIdWriter(int wid){
        String sql = "SELECT wname FROM writertb WHERE wid = ?";
        return jdbcTemplate.queryForObject(sql, String.class, wid);
    }

    @Override
    public String findByIdPublisher(int puid){
        String sql = "SELECT puname FROM putb WHERE puid = ?";
        return jdbcTemplate.queryForObject(sql, String.class, puid);
    }

    @Override
    public List<Category> getCategoryList(){
        String sql = "SELECT cid, category FROM ctb";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        List<Category> cList = new ArrayList<>();
        for(Map<String, Object> result : resultList){
            Category category = new Category();
            category.setCid((int)result.get("cid"));
            category.setCategory((String)result.get("category"));
            cList.add(category);
        }
        return cList;
    }

    @Override
    public List<Kasidasi> getKasidasiList(){
        String sql = "SELECT * FROM kasitb";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        List<Kasidasi> kasiList = new ArrayList<>();
        for(Map<String, Object> result : resultList){
            Kasidasi kasidasi = new Kasidasi();
            kasidasi.setBoid((int)result.get("boid"));
            kasidasi.setBoday((Date)result.get("boday"));
            kasidasi.setMid((int)result.get("mid"));
            kasidasi.setHonid((int)result.get("honid"));
            kasidasi.setReday((Date)result.get("reday"));
            kasidasi.setRem((Boolean)result.get("rem"));
            kasiList.add(kasidasi);
        }
        return kasiList;
    }

    // メソッドの追加
    public int updateBookLent(int honid){
        String sql = "UPDATE booktb SET botype = false WHERE honid = ?";
        System.out.println("***********************" + honid);
        return jdbcTemplate.update(sql, honid);

    }

    public List<DispKasidasi> getShelfBookList(int mid){
        String sql = "SELECT boid,title,boday,reday, photo FROM booktb b JOIN kasitb k ON b.honid=k.honid WHERE k.mid=?";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql,mid);
        List<DispKasidasi> bList = new ArrayList<>();
        for(Map<String, Object> result : resultList){
            DispKasidasi book = new DispKasidasi();
            book.setBoid((int)result.get("boid"));
            book.setTitle((String)result.get("title"));
            book.setBoday((Date)result.get("boday"));
            book.setReday((Date)result.get("reday"));
            book.setPhoto((String)result.get("photo"));
    
            bList.add(book);
        }
        return bList;
    }

}
