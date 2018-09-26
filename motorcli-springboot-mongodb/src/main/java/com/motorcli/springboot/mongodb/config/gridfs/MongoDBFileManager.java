package com.motorcli.springboot.mongodb.config.gridfs;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;

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
     */
    ObjectId save(File file) throws IOException;

    /**
     * 存储文件
     * @param inputStream 文件输入流
     * @param fileName 文件名称
     */
    ObjectId save(InputStream inputStream, String fileName) throws IOException;

    /**
     * 存储文件
     * @param file 文件对象
     * @param params 文件存储参数
     */
    ObjectId save(File file, Map<String, Object> params) throws IOException;

    /**
     * 存储文件
     * @param inputStream 文件输入流
     * @param params 文件存储参数
     * @param fileName 文件名称
     */
    ObjectId save(InputStream inputStream, String fileName, Map<String, Object> params) throws IOException;

    /**
     * 读取到文件
     * @param id 文件ID
     * @return 芒果DB文件对象
     * @throws IOException
     */
    GridFSFile read(String id) throws IOException;

    /**
     * 读取到文件
     * @param id 文件ID
     * @param params 文件参数读取对象
     * @return 芒果DB文件对象
     * @throws IOException
     */
    GridFSFile read(String id, Map<String, Object> params) throws IOException;

    /**
     * 删除文件
     * @param id 文件ID
     */
    void delete(String id);
}
