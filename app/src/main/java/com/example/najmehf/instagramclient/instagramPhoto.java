package com.example.najmehf.instagramclient;

import java.util.ArrayList;

/**
 * Created by najmeh.f on 12/2/2015.
 */
public class instagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int likesCount;
    public ArrayList<String> comments;
    public ArrayList<String> commentsUsers;
    public instagramPhoto(){
        comments=new ArrayList<String>();
        commentsUsers= new ArrayList<String>();
    }


}
