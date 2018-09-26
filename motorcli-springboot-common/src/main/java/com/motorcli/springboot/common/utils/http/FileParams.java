package com.motorcli.springboot.common.utils.http;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HTTP 上传文件参数
 */
@Getter
@Setter
public class FileParams {

    private Map<String, File> fileParams;

    public FileParams() {}

    public FileParams(File file) {
        this.fileParams = new HashMap<>();
        this.fileParams.put(UUID.randomUUID().toString(), file);
    }

    public FileParams(List<File> files) {
        this.fileParams = new HashMap<>();

        for(File file : files) {
            this.fileParams.put(UUID.randomUUID().toString(), file);
        }
    }
}
