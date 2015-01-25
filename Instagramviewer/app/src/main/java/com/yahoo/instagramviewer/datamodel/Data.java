package com.yahoo.instagramviewer.datamodel;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private Caption caption;
    private Comments comments;
    private String created_time;
    private String text;
    private String filter;
    private String id;
    private Images images;
    private Likes likes;
    private String link;
    private List<String> tags;
    private String type;
    private User from;
    private List<Users_in_photo> users_in_photo;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Caption getCaption() {
        return this.caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public Comments getComments() {
        return this.comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public String getCreated_time() {
        return this.created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Images getImages() {
        return this.images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Likes getLikes() {
        return this.likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return this.from;
    }

    public void setUser(User user) {
        this.from = user;
    }

    public List<Users_in_photo> getUsers_in_photo() {
        return this.users_in_photo;
    }

    public void setUsers_in_photo(List<Users_in_photo> users_in_photo) {
        this.users_in_photo = users_in_photo;
    }
}
