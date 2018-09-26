package com.motorcli.springboot.mongodb.config.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.motorcli.springboot.common.utils.CollectionUtils;
import com.motorcli.springboot.common.utils.DateUtils;
import com.motorcli.springboot.common.utils.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Mongo DB 文件服务实现
 */
@Component
public class MongoDBFileManagerImpl implements MongoDBFileManager {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public ObjectId save(File file) throws IOException {
        return this.save(file, null);
    }

    @Override
    public ObjectId save(InputStream inputStream, String fileName) throws IOException {
        return this.save(inputStream, fileName, null);
    }

    @Override
    public ObjectId save(File file, Map<String, Object> params) throws IOException {
        String id = UUID.randomUUID().toString();
        String extension = FileUtils.getExtension(file);


        BasicDBObject metaData = new BasicDBObject();
        metaData.append("filename", file.getName());
        metaData.append("createTime", DateUtils.formatDateTime(new Date()));
        metaData.append("contentType", extension);

        if(CollectionUtils.isEmpty(params)) {
            for(String key : params.keySet()) {
                metaData.append(key, params.get(key));
            }
        }

        return gridFsTemplate.store(new FileInputStream(file), id, metaData);
    }



    @Override
    public ObjectId save(InputStream inputStream, String fileName, Map<String, Object> params) throws IOException {
        String id = UUID.randomUUID().toString();
        String extension = FileUtils.getExtension(fileName);

        BasicDBObject metaData = new BasicDBObject();
        metaData.append("filename", fileName);
        metaData.append("createTime", DateUtils.formatDateTime(new Date()));
        metaData.append("contentType", extension);

        if(CollectionUtils.isEmpty(params)) {
            for(String key : params.keySet()) {
                metaData.append(key, params.get(key));
            }
        }

        return gridFsTemplate.store(inputStream, id, metaData);
    }

    @Override
    public GridFSFile read(String id) throws IOException {
        return this.read(id, null);
    }

    @Override
    public GridFSFile read(String id, Map<String, Object> params) throws IOException {
        return this.gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(id)));
    }

    @Override
    public void delete(String id) {
        this.gridFsTemplate.delete(new Query().addCriteria(Criteria.where("_id").is(id)));
    }
}
