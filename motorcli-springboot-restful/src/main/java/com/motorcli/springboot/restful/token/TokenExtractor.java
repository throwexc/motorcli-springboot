package com.motorcli.springboot.restful.token;

/**
 * 实现这个接口应该返回原Base-64编码
 * 表示Token
 */
public interface TokenExtractor {
    String extract(String payload);
}
