package com.penglecode.flink.common.model;

import java.io.Serializable;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 22:37
 */
public class Joke implements Serializable {

    private String sid;

    private String text;

    private String type;

    private String thumbnail;

    private String images;

    private String video;

    private Integer up;

    private Integer down;

    private Integer forward;

    private Integer comment;

    private String passtime;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public Integer getForward() {
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "sid='" + sid + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", images='" + images + '\'' +
                ", video='" + video + '\'' +
                ", up=" + up +
                ", down=" + down +
                ", forward=" + forward +
                ", comment=" + comment +
                ", passtime='" + passtime + '\'' +
                '}';
    }

}
