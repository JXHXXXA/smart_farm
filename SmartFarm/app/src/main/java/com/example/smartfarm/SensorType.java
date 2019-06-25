package com.example.smartfarm;

import java.util.Arrays;
import java.util.List;

public enum SensorType {

    DASHBOARD(Arrays.asList("실내온도1","실내온도2","실내온도3","실내온도4",
            "온수온도",
            "이산화탄소1","이산화탄소2","이산화탄소3","이산화탄소4","이산화탄소5","이산화탄소6",
            "습도1","습도2","습도3","습도4","습도5","습도6",
            "전자저울1","전자저울2","전자저울3","전자저울4","전자저울5","전자저울6",
            "조명"),
            Arrays.asList(2,3,4,5,
                    6,
                    8,9,10,11,12,13,
                    15,16,17,18,19,20,
                    21,22,23,24,25,26,
                    27)
    ),
    DASHBOARD_AVG(Arrays.asList("실내온도평균","온수온도","이산화탄소평균","습도평균", "조명"),
            Arrays.asList(1,6,7,14,27)
    );

    private List<String> nameList;
    private List<Integer> valueList;

    SensorType(List<String> nameList, List<Integer> valueList) {
        this.nameList = nameList;
        this.valueList = valueList;
    }
    public static boolean hasSensorType(String sensor_id){
        return DASHBOARD.valueList.contains(Integer.parseInt(sensor_id));
    }

    public static String getSensorName(String sensor_id){
        for(int i=0; i<DASHBOARD.valueList.size(); i++){
            if(sensor_id.equals(Integer.toString(DASHBOARD.valueList.get(i))))
                return DASHBOARD.nameList.get(i);
        }
        return "";
    }
}