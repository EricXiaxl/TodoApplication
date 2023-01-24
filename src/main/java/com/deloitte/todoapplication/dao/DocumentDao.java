package com.deloitte.todoapplication.dao;

import com.deloitte.todoapplication.pojo.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDao extends JpaRepository<Document, Long> {

    @Query("select d from Document d where d.id = ?1")
    Document findDocumentById(Long id);
}
