package com.rajasahabacademy.model;

public class HomeCourseModel {
    String image;
    String name;
    String catId = "0";
    boolean allCatFlag = false;
    boolean selectCat = false;

    public HomeCourseModel(String image, String name,String catId ,boolean allCatFlag) {
        this.image = image;
        this.name = name;
        this.catId = catId;
        this.allCatFlag = allCatFlag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllCatFlag() {
        return allCatFlag;
    }

    public void setAllCatFlag(boolean allCatFlag) {
        this.allCatFlag = allCatFlag;
    }

    public boolean isSelectCat() {
        return selectCat;
    }

    public void setSelectCat(boolean selectCat) {
        this.selectCat = selectCat;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
