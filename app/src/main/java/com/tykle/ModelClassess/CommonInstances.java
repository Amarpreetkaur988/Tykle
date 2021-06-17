package com.tykle.ModelClassess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omninossolutions on 05/10/18.
 */

public class CommonInstances {

    String userName, mobile;

    private List<String> imagesPathList = new ArrayList<>();

    public List<String> getImagesPathList() {
        return imagesPathList;
    }

    public void setImagesPathList(List<String> imagesPathList) {
        this.imagesPathList = imagesPathList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }




}
