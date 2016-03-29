package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by Mohamed on 28/03/2016.
 */
public class Status extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.train_transport, null, false);
        mDrawer.addView(contentView, 0);


        final TabHost host = (TabHost)findViewById(R.id.tabHost);


        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Now");
        spec.setContent(R.id.listViewShop);
        spec.setIndicator("Now");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("This Weekend");
        spec.setContent(R.id.listViewBar);
        spec.setIndicator("This Weekend");
        host.addTab(spec);




   //     final ListView lv = (ListView) findViewById(R.id.listViewStatus);

    }

}
