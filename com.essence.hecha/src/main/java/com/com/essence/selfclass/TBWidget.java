package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/18.
 */

public class TBWidget{

        private boolean showFlag;
        private int ID;
        private String Name;
        private String ControlType;
        private String DefaultValue;
        private String MinValue;
        private String MaxValue;
        private String DataType;
        private String Required;
        private String[] Options;

        public int getID() {
            return ID;
        }
        public void setID(int iD) {
            ID = iD;
        }
        public String getName() {
            return Name;
        }
        public void setName(String name) {
            Name = name;
        }
        public String getControlType() {
            return ControlType;
        }
        public void setControlType(String controlType) {
            ControlType = controlType;
        }
        public String getDefaultValue() {
            return DefaultValue;
        }
        public void setDefaultValue(String defaultValue) {
            DefaultValue = defaultValue;
        }
        public String getMinValue() {
            return MinValue;
        }
        public void setMinValue(String minValue) {
            MinValue = minValue;
        }
        public String getMaxValue() {
            return MaxValue;
        }
        public void setMaxValue(String maxValue) {
            MaxValue = maxValue;
        }
        public String getDataType() {
            return DataType;
        }
        public void setDataType(String dataType) {
            DataType = dataType;
        }
        public String getRequired() {
            return Required;
        }
        public void setRequired(String required) {
            Required = required;
        }

        public String[] getOptions() {
            return Options;
        }
        public void setOptions(String[] options) {
            Options = options;
        }


        public Boolean getShowFlag() {
            return showFlag;
        }
        public void setShowFlag(Boolean showFlag) {
            this.showFlag = showFlag;
        }


}
