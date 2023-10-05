package com.gam.module.ads.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.gam.module.ads.R;
import com.gam.module.ads.fragment.BlankFragment;

import java.util.List;

public class ContentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        showFragment(new BlankFragment(),"BlankFragment");
    }


    public void showFragment(Fragment fragment,String tag) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment1 != null && fragment1.isAdded()) {
                ft.show(fragment1);
            } else {
                ft.add(R.id.flMain, fragment, tag);
            }
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments.size() > 0) {
                for (Fragment frag : fragments) {
                    if (frag != fragment1) {
                        if (frag.isAdded())
                            ft.hide(frag);
                    }
                }
            }
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}