package com.gdut.library.makemake;

import android.app.Activity;
import android.os.Bundle;

import com.gdut.library.makemake.apihelper.ApiHelper;

public class MakemakeActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ApiHelper helper = new ApiHelper();
    }
}
