package com.motorcli.springboot.mongodb.config.gridfs;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Mongo DB 文件服务
 */
public interface MongoDBFileManager {

    /**
     * 存储文件
     * @param file 文件对象
     * @return 文件ID
     */
    ObjectId save(File file);

    /**
     * 存储文件
     * @param inputStream 文件输入流
     * @param fileName 文件名称
     * @return 文件ID
     */
    ObjectId save(InputStream inputStream, String fileName);

    /**
     * 存储文件
     * @param file 文件对象
     * @param params 文件存储参数
     * @return 文件ID
     */
    ObjectId save(File file, Map<String, Object> params);

    /**
     * 存储文件
     * @param inputStream 文件输入流
     * @param params 文件存储参数
     * @param fileName 文件名称
     * @return 文件ID
     */
    ObjectId save(InputStream inputStream, String fileName, Map<String, Object> params);

    /**
     * 读取到文件
     * @param id 文件ID
     * @return MongoDB 文件对象
     * @throws IOException
     */
    GridFSFile get(ObjectId id);

    /**
     * 读取到文件
     * @param id 文件ID
     * @param params 文件参数读取对象
     * @return MongoDB 文件对象
     * @throws IOException
     */
    GridFSFile get(ObjectId id, Map<String, Object> params);

    /**
     * 获取文件资源
     * @param fileName 文件名称
     * @return MongoDB 文件资源对象
     */
    GridFsResource getResource(String fileName);

    /**
     * 删除文件
     * @param id 文件ID
     */
    void delete(ObjectId id);
}
