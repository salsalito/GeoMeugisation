package com.meuge.geolocalisation;

import android.content.Intent;
import android.os.Bundle;

import com.tools.meuge.TabGroupActivity;

public class InfosActivityGroup extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("Infos", new Intent(this,InfosActivity.class));
    }
}
