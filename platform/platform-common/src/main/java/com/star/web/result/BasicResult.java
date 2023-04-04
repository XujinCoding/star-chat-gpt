package com.star.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author XuJ
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BasicResult implements Serializable {

    private Boolean status;
    /**
     * http 编码
     */
    private String code;
    /**
     * 前端使用
     */
    private String message;
    private Object data;

    /**
     * 提供给前端异常信息参数
     */
    private List<String> arguments;

}
