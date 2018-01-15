package com.dingtai.jinriyongzhou.bean;

import com.dingtai.dtflash.model.OpenPicModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class StartPics {

    private List<OpenPicBean> OpenPic;

    public List<OpenPicBean> getOpenPic() {
        return OpenPic;
    }

    public void setOpenPic(List<OpenPicBean> OpenPic) {
        this.OpenPic = OpenPic;
    }

    public static class OpenPicBean implements Serializable {
        /**
         * ID : MTk=
         * CreateTime : MjAxNS0wOC0wOCAxNTowODozMw==
         * StID : MA==
         * ChID : MA==
         * ForApp : OQ==
         * RandomNum : MmQ2NmIyYjctMGFhYS00ZWFlLWJkYmUtNzdiYWM1MjkxZTY5
         * Status : MQ==
         * OpenPicName : MQ==
         * ImgUrl : aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE3MTIyMTEzMjgzOTg3Nzg1NzAwMC5qcGc=
         * LinkUrl :
         * ADFor : LTE=
         * ADContent :
         * LinkTo : LTE=
         * ImgUrls : aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE3MTIyMTEzMjgzOTg3Nzg1NzAwMC5qcGc=
         * OpenTime : NQ==
         * OpenPicDetail : [{"ID":"MjU=","CreateTime":"MjAxNy0xMS0yMiAwOTozOToxMg==","OpenPicName":"MQ==","ImgUrl":"aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE3MTIyMTEzMjgzOTg3Nzg1NzAwMC5qcGc=","LinkUrl":"","ADFor":"LTE=","ADContent":"","LinkTo":"LTE=","ImgUrls":"aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE3MTIyMTEzMjgzOTg3Nzg1NzAwMC5qcGc=","ReMark":""},{"ID":"MjY=","CreateTime":"MjAxNy0xMS0yMiAwOTozOToxMg==","OpenPicName":"Mg==","ImgUrl":"aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE4MDExMzEwMzEzMDQzNTk1MjAwMC5qcGc=","LinkUrl":"","ADFor":"LTE=","ADContent":"","LinkTo":"LTE=","ImgUrls":"aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE4MDExMzEwMzEzMDQzNTk1MjAwMC5qcGc=","ReMark":""}]
         */

        private String ID;
        private String CreateTime;
        private String StID;
        private String ChID;
        private String ForApp;
        private String RandomNum;
        private String Status;
        private String OpenPicName;
        private String ImgUrl;
        private String LinkUrl;
        private String ADFor;
        private String ADContent;
        private String LinkTo;
        private String ImgUrls;
        private String OpenTime;
        private List<OpenPicModel> OpenPicDetail;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getStID() {
            return StID;
        }

        public void setStID(String StID) {
            this.StID = StID;
        }

        public String getChID() {
            return ChID;
        }

        public void setChID(String ChID) {
            this.ChID = ChID;
        }

        public String getForApp() {
            return ForApp;
        }

        public void setForApp(String ForApp) {
            this.ForApp = ForApp;
        }

        public String getRandomNum() {
            return RandomNum;
        }

        public void setRandomNum(String RandomNum) {
            this.RandomNum = RandomNum;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getOpenPicName() {
            return OpenPicName;
        }

        public void setOpenPicName(String OpenPicName) {
            this.OpenPicName = OpenPicName;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String ImgUrl) {
            this.ImgUrl = ImgUrl;
        }

        public String getLinkUrl() {
            return LinkUrl;
        }

        public void setLinkUrl(String LinkUrl) {
            this.LinkUrl = LinkUrl;
        }

        public String getADFor() {
            return ADFor;
        }

        public void setADFor(String ADFor) {
            this.ADFor = ADFor;
        }

        public String getADContent() {
            return ADContent;
        }

        public void setADContent(String ADContent) {
            this.ADContent = ADContent;
        }

        public String getLinkTo() {
            return LinkTo;
        }

        public void setLinkTo(String LinkTo) {
            this.LinkTo = LinkTo;
        }

        public String getImgUrls() {
            return ImgUrls;
        }

        public void setImgUrls(String ImgUrls) {
            this.ImgUrls = ImgUrls;
        }

        public String getOpenTime() {
            return OpenTime;
        }

        public void setOpenTime(String OpenTime) {
            this.OpenTime = OpenTime;
        }

        public List<OpenPicModel> getOpenPicDetail() {
            return OpenPicDetail;
        }

        public void setOpenPicDetail(List<OpenPicModel> OpenPicDetail) {
            this.OpenPicDetail = OpenPicDetail;
        }


    }
}
