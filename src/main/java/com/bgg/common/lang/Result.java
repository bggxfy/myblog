package com.bgg.common.lang;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain =true)
public class Result implements Serializable {

    private int code;   //200：正常    非200表示异常
    private String msg;
    private Object data;

    public static Result success(Object data){
        return success(200,"操作成功！",data);
    }

    public static Result success(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code).setMsg(msg).setData(data);
        return result;
    }

    public static Result fail(String msg){
        return fail(400,msg,null);
    }

    public static Result fail(String msg,Object data){
        return fail(400,msg,data);
    }

    public static Result fail(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code).setMsg(msg).setData(data);
        return result;
    }


}
