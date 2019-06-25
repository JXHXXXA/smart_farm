package com.example.smartfarm;

import java.util.Arrays;
import java.util.List;

public enum SettingType {

    ONOFF("ON/OFF",
            Arrays.asList(1,2,3,4,5,6,
                    22,26,27,28,29,30,
                    52,53,
                    54,55,56,57,58,59,
                    60,61,62,63,64)
    ),
    THRESHOLD("임계값",
            Arrays.asList(
                    8,9,
                    11,12,
                    14,15,
                    17,18,
                    20,21,
                    24,25,
                    32,33,
                    35,36,
                    38,39,
                    41,42,
                    44,45,
                    47,48,
                    50,51
                    )
    ),
    VALUESET("설정값",
            Arrays.asList(7,10,13,16,19,23,49,
                    31,34,37,40,43,46)
    );

    private String name;
    private List<Integer> idList;

    SettingType(String name, List<Integer> idList) {
        this.name = name;
        this.idList = idList;
    }
    public static boolean hasOnOffSetting(String setting_id){
        return ONOFF.idList.contains(Integer.parseInt(setting_id));
    }
    public static boolean hasThresholdSetting(String setting_id){
        return THRESHOLD.idList.contains(Integer.parseInt(setting_id));
    }
    public static boolean hasValueSetting(String setting_id){
        return VALUESET.idList.contains(Integer.parseInt(setting_id));
    }

}