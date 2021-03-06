/*
 * Copyright (C) 2013 The SlimRoms Project
 * Modifications Copyright (C) 2014 The NamelessRom Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.quicksettings;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.android.internal.util.mahdi.ButtonsConstants;
import com.android.internal.util.mahdi.MahdiActions;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class ThemeTile extends QuickSettingsTile {

    private static final int THEME_MODE_MANUAL = 0;
    private static final int THEME_MODE_LIGHT_SENSOR = 1;
    private static final int THEME_MODE_TWILIGHT = 2;

    private int mThemeAutoMode;

    public ThemeTile(Context context, QuickSettingsController qsc) {
        super(context, qsc);

        mOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MahdiActions.processAction(mContext, ButtonsConstants.ACTION_THEME_SWITCH, false);
            }
        };

        mOnLongClick = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent("android.settings.THEME_SETTINGS");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startSettingsActivity(intent);
                return true;
            }
        };

        qsc.registerObservedContent(Settings.Secure.getUriFor(
                Settings.Secure.UI_THEME_AUTO_MODE), this);
    }

    @Override
    public void onFlingRight() {
        if (mThemeAutoMode == THEME_MODE_TWILIGHT) {
            mThemeAutoMode = THEME_MODE_MANUAL;
        } else {
            mThemeAutoMode += 1;
        }
        saveChanges();
        super.onFlingRight();
    }

    @Override
    public void onFlingLeft() {
        if (mThemeAutoMode == THEME_MODE_MANUAL) {
            mThemeAutoMode = THEME_MODE_TWILIGHT;
        } else {
            mThemeAutoMode -= 1;
        }
        saveChanges();
        super.onFlingLeft();
    }

    private void saveChanges() {
        Settings.Secure.putIntForUser(mContext.getContentResolver(),
                Settings.Secure.UI_THEME_AUTO_MODE, mThemeAutoMode,
                UserHandle.USER_CURRENT);
    }

    @Override
    void onPostCreate() {
        updateTile();
        super.onPostCreate();
    }

    @Override
    public void updateResources() {
        updateTile();
        super.updateResources();
    }

    @Override
    public void onChangeUri(ContentResolver resolver, Uri uri) {
        updateResources();
    }

    private synchronized void updateTile() {
        mThemeAutoMode = Settings.Secure.getIntForUser(mContext.getContentResolver(),
                Settings.Secure.UI_THEME_AUTO_MODE, THEME_MODE_MANUAL, UserHandle.USER_CURRENT);

        switch (mThemeAutoMode) {
            case THEME_MODE_MANUAL:
                mDrawable = R.drawable.ic_qs_theme_manual;
                break;
            case THEME_MODE_LIGHT_SENSOR:
                mDrawable = R.drawable.ic_qs_theme_lightsensor;
                break;
            case THEME_MODE_TWILIGHT:
                mDrawable = R.drawable.ic_qs_theme_twilight;
                break;
        }

        if (mContext.getResources().getConfiguration().uiThemeMode
                == Configuration.UI_THEME_MODE_HOLO_DARK) {
            mLabel = mContext.getString(R.string.quick_settings_theme_switch_dark);
        } else {
            mLabel = mContext.getString(R.string.quick_settings_theme_switch_light);
        }
    }

}
