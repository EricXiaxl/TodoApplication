package com.deloitte.todoapplication.service.impl;

import com.deloitte.todoapplication.dao.DocumentDao;
import com.deloitte.todoapplication.pojo.Document;
import com.deloitte.todoapplication.service.DocumentService;
import com.deloitte.todoapplication.util.DocumentUtil;
import com.deloitte.todoapplication.util.IDGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.Instant;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    DocumentDao documentDao;

    @Override
    public Document saveDocument(MultipartFile file, String userId) {
        try{
            // analyse file
            String fileName = file.getOriginalFilename(); //document Name
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1); //fileType of document
            Document document = new Document();
            document.setUploadorId(Long.valueOf(userId));
            document.setFileName(fileName);
            document.setFileType(prefix);
            document.setSize(file.getSize());
            String aliasName = IDGeneratorUtil.getMessageID()+"."+prefix;
            document.setAliasName(aliasName);

            byte[] data = DocumentUtil.convertFileToByteArray(file); //convert the file into a byte array
            if(data==null){
                throw new Exception("parsing file exception");
            }
            document.setData(data);
            document.setCreateTime(Instant.now());
            return documentDao.save(document);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
