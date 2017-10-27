package com.lw.wechatplugin;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lw.wechatplugin.utils.WxUtils;
import com.lw.wechatplugin.vo.WxContactVo;

import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment mSettingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
            List<WxContactVo> wxContactVoList = WxUtils.getInstance().fetchPublicAccountList(this);
            if(wxContactVoList != null && wxContactVoList.size() > 0){
                for(int i=0;i<wxContactVoList.size();i++){
                    Log.d("Setting", wxContactVoList.get(i).toString());
                }
            }else{
                Log.d("Setting", "wxContact read null=====================");
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }


    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.pref_setting);

//            EditTextPreference editPref = (EditTextPreference) findPreference("transfer_setting");
//            editPref.getEditText().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String str = CommonUtils.getTulingReply("你好啊");
//                    System.out.println(str);
//                }
//            }).start();

        }
    }
}
