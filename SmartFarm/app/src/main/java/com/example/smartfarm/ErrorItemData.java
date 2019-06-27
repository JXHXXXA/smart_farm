package com.example.smartfarm;

public class ErrorItemData {
    public String sensor_type;
    public String error_info;
    public String time_stamp;
    public String repair_time;

    public String getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(String sensor_type) {
        this.sensor_type = sensor_type;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getRepair_time() {
        return repair_time;
    }

    public void setRepair_time(String repair_time) {
        this.repair_time = repair_time;
    }
}
