package com.timon.web;

import lombok.Builder;
import lombok.Data;

//@Builder.Default
@Data
public class WebResponse<T> {
    private boolean success = true;
    private String err_code = "";
    private String err_msg = "";
    private T data;

    /**
     * 默认成功
     */
    public WebResponse() {
    }

    public WebResponse(String err_msg){
        this.err_msg = err_msg;
    }

    /**
     * 成功
     */
    public WebResponse(T data) {
        this.data = data;
    }


}
