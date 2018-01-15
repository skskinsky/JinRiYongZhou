package com.dingtai.jinriyongzhou.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class DianBoDetialListBean2 {

    private List<VODChannelsContentBean> VODChannelsContent;

    public List<VODChannelsContentBean> getVODChannelsContent() {
        return VODChannelsContent;
    }

    public void setVODChannelsContent(List<VODChannelsContentBean> VODChannelsContent) {
        this.VODChannelsContent = VODChannelsContent;
    }

    public static class VODChannelsContentBean {
        /**
         * ID : NTQw
         * CreateTime : MjAxNy8xMS8xNiAxMDowNToyMg==
         * NewDateTime : MTHmnIgxNuaXpQ==
         * ProgramName : 6IiM5bCW5LiK55qE5bm456aP5rC45bee
         * ProgramHornLogo : 
         * ProgramLogo : aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy9NaWRUaHVtYm5haWwvMjAxNzExMTYxMDAyMTg2OTg2NzUwMDAuanBn
         * IsRec : VHJ1ZQ==
         * VODType : MQ==
         * VODChID : MTE0
         * Status : MQ==
         * ReMark : 
         */

        private String ID;
        private String CreateTime;
        private String NewDateTime;
        private String ProgramName;
        private String ProgramHornLogo;
        private String ProgramLogo;
        private String IsRec;
        private String VODType;
        private String VODChID;
        private String Status;
        private String ReMark;

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

        public String getNewDateTime() {
            return NewDateTime;
        }

        public void setNewDateTime(String NewDateTime) {
            this.NewDateTime = NewDateTime;
        }

        public String getProgramName() {
            return ProgramName;
        }

        public void setProgramName(String ProgramName) {
            this.ProgramName = ProgramName;
        }

        public String getProgramHornLogo() {
            return ProgramHornLogo;
        }

        public void setProgramHornLogo(String ProgramHornLogo) {
            this.ProgramHornLogo = ProgramHornLogo;
        }

        public String getProgramLogo() {
            return ProgramLogo;
        }

        public void setProgramLogo(String ProgramLogo) {
            this.ProgramLogo = ProgramLogo;
        }

        public String getIsRec() {
            return IsRec;
        }

        public void setIsRec(String IsRec) {
            this.IsRec = IsRec;
        }

        public String getVODType() {
            return VODType;
        }

        public void setVODType(String VODType) {
            this.VODType = VODType;
        }

        public String getVODChID() {
            return VODChID;
        }

        public void setVODChID(String VODChID) {
            this.VODChID = VODChID;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getReMark() {
            return ReMark;
        }

        public void setReMark(String ReMark) {
            this.ReMark = ReMark;
        }
    }
}
