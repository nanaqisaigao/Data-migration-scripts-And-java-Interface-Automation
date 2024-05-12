package com.hellobike.pmo.cockpit.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author renmengxiwb304
 * @date 2024/2/16
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Proto implements Serializable {


    // private static final long serialVersionUID = 2720168028485411265L;
    public Integer code;

    public Boolean success;

    public String msg;

    public Object data;


    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public void setData(Object data){
        this.data = data;
    }

    public void setCode(Integer code){
        this.code= code;
    }


    public static Proto success(Object data) {
        Proto htDevResponse = new Proto();
        htDevResponse.setCode(0);
        htDevResponse.setMsg("请求成功");
        htDevResponse.setSuccess(true);
        htDevResponse.setData(data);
        return htDevResponse;
    }

    public static Proto fail(Object data, String msg) {
        Proto htDevResponse = new Proto();
        htDevResponse.setCode(1000);
        htDevResponse.setMsg(msg);
        htDevResponse.setSuccess(false);
        htDevResponse.setData(data);
        return htDevResponse;
    }
}

