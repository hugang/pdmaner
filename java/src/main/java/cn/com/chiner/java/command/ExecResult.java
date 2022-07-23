package cn.com.chiner.java.command;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 执行命令的返回结果对象
 */
@JsonPropertyOrder({
        "status",
        "body",
        "properties",
})
public class ExecResult implements Serializable {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    private String status = SUCCESS;
    private Object body;
    private Map<String,Object> properties = new HashMap<String,Object>();

    public ExecResult() {
    }

    public ExecResult(String status, String body) {
        this.status = status;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
