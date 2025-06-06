package whu.english_learn.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 封装通用的返回结果类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;       // 状态码，通常用于标识请求是否成功
    private String message;    // 描述信息
    private Object data;       // 返回的具体数据，使用 Object 类型来表示可以是任何类型的数据

    // 成功的返回结果，不带数据
    public static Result success(String message) {
        return new Result(1, message, null);
    }

    // 成功的返回结果，带数据
    public static Result success(String message, Object data) {
        return new Result(1, message, data);
    }

    // 成功的返回结果，仅数据
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    // 失败的返回结果
    public static Result error(String message) {
        return new Result(0, message, null);
    }

    // 失败的返回结果，带数据
    public static Result error( String message, Object data) {
        return new Result(0, message, data);
    }
}