package cn.kgc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;   //后端返回的状态码，20000--->成功,否则代表失败
    private String message; //后端返回的消息
    private Object data;    //后端返回的最终结果

    //登录成功调用的方法
    public static Result success(Object data){
        return new Result(StatusCode.OK,"执行成功",data);
    }
    //登录失败调用的方法
    public static Result fail(Integer code,String message){
        return new Result(code,message,null);
    }
}
