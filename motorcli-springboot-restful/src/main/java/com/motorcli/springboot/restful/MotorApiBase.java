package com.motorcli.springboot.restful;

import com.motorcli.springboot.restful.result.Result;
import com.motorcli.springboot.restful.result.ResultItems;
import com.motorcli.springboot.restful.result.ResultRecord;
import com.motorcli.springboot.web.MotorWebControllerBase;

import java.util.List;

public class MotorApiBase extends MotorWebControllerBase {

    //----------------------------------

    protected Result getResult() {
        return this.getResult(SUCCESS_CODE, SUCCESS_MSG);
    }

    protected Result getResult(String msg) {
        return this.getResult(SUCCESS_CODE, msg);
    }

    protected Result getResult(int code, String msg) {
        return new Result(code, msg);
    }

    //----------------------------------

    protected <T> ResultRecord<T> getResult(T record) {
        return this.getResult(SUCCESS_CODE, SUCCESS_MSG, record);
    }

    protected <T> ResultRecord<T> getResult(String msg, T record) {
        return this.getResult(SUCCESS_CODE, msg, record);
    }

    protected <T> ResultRecord<T> getResult(int code, String msg, T record) {
        return new ResultRecord<>(code, msg, record);
    }

    //----------------------------------

    protected <T> ResultItems<T> getResult(List<T> items) {
        return this.getResult(SUCCESS_CODE, SUCCESS_MSG, items);
    }

    protected <T> ResultItems<T> getResult(String msg, List<T> items) {
        return this.getResult(SUCCESS_CODE, msg, items);
    }

    protected <T> ResultItems<T> getResult(int code, String msg, List<T> items) {
        return new ResultItems<>(code, msg, items);
    }

    //----------------------------------

    protected <T> ResultItems<T> getResult(List<T> items, long total) {
        return this.getResult(SUCCESS_CODE, SUCCESS_MSG, items, null, total , null);
    }

    protected <T> ResultItems<T> getResult(String msg, List<T> items, long total) {
        return this.getResult(SUCCESS_CODE, msg, items, null, total , null);
    }

    protected <T> ResultItems<T> getResult(int code, String msg, List<T> items, long total) {
        return this.getResult(code, msg, items, null, total , null);
    }

    //----------------------------------

    protected <T> ResultItems<T> getResult(List<T> items, Integer page, Long total, Integer totalPage) {
        return this.getResult(SUCCESS_CODE, SUCCESS_MSG, items, page, total, totalPage);
    }

    protected <T> ResultItems<T> getResult(String msg, List<T> items, Integer page, Long total, Integer totalPage) {
        return this.getResult(SUCCESS_CODE, msg, items, page, total, totalPage);
    }

    protected <T> ResultItems<T> getResult(int code, String msg, List<T> items, Integer page, Long total, Integer totalPage) {
        return new ResultItems<>(code, msg, items, page, total, totalPage);
    }

    //----------------------------------

    protected <X extends Result> X getResult(Class<X> clz, int code, String msg) {
        return this.getResult(clz, code, msg);
    }
}
