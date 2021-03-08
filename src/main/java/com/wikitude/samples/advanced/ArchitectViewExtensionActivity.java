package com.wikitude.samples.advanced;

import com.wikitude.architect.ArchitectView;
import com.wikitude.samples.SimpleArActivity;
import com.wikitude.samples.util.SampleData;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ArchitectViewExtensionActivity extends SimpleArActivity {


    private static final String EXTENSION_GEO = "geo";


    private final Map<String, ArchitectViewExtension> extensions = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        SampleData sampleData = (SampleData) intent.getSerializableExtra(SimpleArActivity.INTENT_EXTRAS_KEY_SAMPLE);

        for (String extension : sampleData.getExtensions()) {
            switch (extension) {

                case EXTENSION_GEO:
                    extensions.put(EXTENSION_GEO, new GeoExtension(this, architectView));
                    break;

            }
        }


        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onCreate();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onPostCreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onResume();
        }
    }

    @Override
    protected void onPause() {
        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onPause();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onDestroy();
        }
        extensions.clear();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArchitectView.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
