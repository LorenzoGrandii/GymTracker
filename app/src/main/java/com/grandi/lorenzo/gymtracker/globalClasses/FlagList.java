package com.grandi.lorenzo.gymtracker.globalClasses;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class FlagList {
    public List<Integer> defaultFlag;

    public FlagList(Intent intent) {
        defaultFlag = new ArrayList<Integer>();
        defaultFlag.add(0, Intent.FLAG_ACTIVITY_NO_ANIMATION);
        defaultFlag.add(1, Intent.FLAG_ACTIVITY_NO_HISTORY);
        for (int i = 0; i < defaultFlag.size(); i++) {
            intent.addFlags(defaultFlag.get(i));
        }
    }
}
