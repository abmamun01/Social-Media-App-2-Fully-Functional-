package com.mamunsproject.socialmediaapp2.Model;

public class All_User_Member {

    String name,uid,prof,url;

    public All_User_Member() {
    }

    public All_User_Member(String name, String uid, String prof, String url) {
        this.name = name;
        this.uid = uid;
        this.prof = prof;
        this.url = url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
