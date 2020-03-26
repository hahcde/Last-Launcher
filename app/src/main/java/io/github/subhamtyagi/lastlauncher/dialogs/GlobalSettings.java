/*
 * Last Launcher
 * Copyright (C) 2019 Shubham Tyagi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.subhamtyagi.lastlauncher.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import io.github.subhamtyagi.lastlauncher.LauncherActivity;
import io.github.subhamtyagi.lastlauncher.R;
import io.github.subhamtyagi.lastlauncher.util.DbUtils;
import io.github.subhamtyagi.lastlauncher.util.SpUtils;

public class GlobalSettings extends Dialog implements View.OnClickListener {


    private TextView freezeSize;
    private LauncherActivity launcherActivity;

    public GlobalSettings(Context context, LauncherActivity launcherActivity) {
        super(context);
        this.launcherActivity = launcherActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_global_settings);

        findViewById(R.id.settings_fonts).setOnClickListener(this);
        findViewById(R.id.settings_themes).setOnClickListener(this);

        freezeSize = findViewById(R.id.settings_freeze_size);
        freezeSize.setOnClickListener(this::onClick);

        findViewById(R.id.settings_reset_to_defaults).setOnClickListener(this);
        findViewById(R.id.settings_backup).setOnClickListener(this);
        findViewById(R.id.settings_restore).setOnClickListener(this);
        //findViewById(R.id.settings_primary_color).setOnClickListener(this);
        TextView randomColors = findViewById(R.id.settings_setup_random_colors);
        randomColors.setOnClickListener(this);
        findViewById(R.id.settings_freezed_apps).setOnClickListener(this);
        findViewById(R.id.settings_hidden_apps).setOnClickListener(this);

        if (DbUtils.isRandomColor()) {
            randomColors.setText(R.string.fixed_colors);
        } else
            randomColors.setText(R.string.random_colors);

        if (DbUtils.isSizeFreezed()) {
            freezeSize.setText(R.string.unfreeze_app_size);
        } else
            freezeSize.setText(R.string.freeze_apps_size);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_fonts:
                setFonts();
                break;
            case R.id.settings_themes:
                bgColor();
                break;
            case R.id.settings_freeze_size:
                freezeAppsSize();
                break;
            case R.id.settings_backup:
                backup();
                break;
            case R.id.settings_restore:
                restore();
                break;
            case R.id.settings_primary_color:
                setPrimaryColor();
                break;
            case R.id.settings_reset_to_defaults:
                defaultSettings();
                break;
            case R.id.settings_setup_random_colors:
                randomColor();
                break;
            case R.id.settings_hidden_apps:
                hiddenApps();
                break;
            case R.id.settings_freezed_apps:
                freezedApps();
        }
    }

    private void freezeAppsSize() {
        boolean b = DbUtils.isSizeFreezed();
        DbUtils.freezeSize(!b);
        if (!b) {
            freezeSize.setText(R.string.unfreeze_app_size);
        } else
            freezeSize.setText(R.string.freeze_apps_size);
    }

    private void freezedApps() {
        launcherActivity.showFreezedApps();
        cancel();
    }

    private void hiddenApps() {
        launcherActivity.showHiddenApps();
        cancel();
    }

    private void randomColor() {
        DbUtils.randomColor(!DbUtils.isRandomColor());
        cancel();
        launcherActivity.recreate();
    }

    private void bgColor() {
        cancel();
        new ThemeSelector(getContext(), launcherActivity).show();
    }


    private void defaultSettings() {
        SpUtils.getInstance().clear();
        launcherActivity.recreate();
    }

    private void setPrimaryColor() {
        Toast.makeText(getContext(), "Not implemnted yet", Toast.LENGTH_SHORT).show();
    }

    private void backup() {
        if (launcherActivity.isPermissionRequired())
            launcherActivity.requestPermission();
        else {
            boolean b = SpUtils.getInstance().saveSharedPreferencesToFile();
            cancel();
            Toast.makeText(getContext(), b ? "Backup saved to Download" : "Some error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void restore() {
        if (launcherActivity.isPermissionRequired())
            launcherActivity.requestPermission();
        else {
            launcherActivity.browseFile();
            cancel();
            Toast.makeText(getContext(), "Choose old backup file", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFonts() {
        if (launcherActivity.isPermissionRequired())
            launcherActivity.requestPermission();
        else {
            launcherActivity.browseFonts();
            cancel();
        }


    }

}
