package com.anjuxing.platform.security.oauth2.handler;

/**
 * @author xiongt
 * @Description
 */
public class OAuth2Response<T> {

    private T content;

    private int code = -1;


    public OAuth2Response(T content){
        this.content = content;
    }

    public OAuth2Response(T content , int code){
        this.content = content;
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
