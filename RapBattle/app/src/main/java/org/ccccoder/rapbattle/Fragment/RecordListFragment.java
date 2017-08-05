package org.ccccoder.rapbattle.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import org.ccccoder.rapbattle.Adapter.ItemAdapter;
import org.ccccoder.rapbattle.Adapter.ItemAnimator;
import org.ccccoder.rapbattle.Model.Record;
import org.ccccoder.rapbattle.R;
import org.ccccoder.rapbattle.Util.Utils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class RecordListFragment extends Fragment {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    FloatingActionButton fabCreate;

    CoordinatorLayout clContent;
    ItemAdapter mAdapter;
    private boolean pendingIntroAnimation;
    //List<ItemModel> models;
    RealmResults<Record> models;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Realm realm;  //DataBase
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_recordlist, container, false);
        mFab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        realm = Realm.getDefaultInstance();
        preset2();
        models = realm.where(Record.class).findAll();
        /*RecycleView 설정*/
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAdapter = new ItemAdapter(getActivity().getApplicationContext(), null, models);
        mRecyclerView.setItemAnimator(new ItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }


        return view;

    }

    private void preset2(){
        Record new_item = new Record();
        new_item.setRecord_id(System.currentTimeMillis());
        new_item.setUser_Lv(100);
        new_item.setUser_nickname("마른새우");
        new_item.setTitleName("소나무");
        new_item.setUser_lyrics("두리번 두리번\\n누가 내 삶 가져갔어\\n전부 도둑놈 같아\\n매일 들이닥치는 자연재해\\n난 손도 못 쓰고 당해\\n일 더하고 일해 \\nBut they call me \\n 귀요미 Thang\\n내 랩이 어쩌고 저쩌고\\n나불대네 고맙다");
        new_item.setIsLikes(false); //
        new_item.setCount_likes(33); // 서버 업데이트

        new_item.setIsCustom(false);
        new_item.setRegister_datetime(getFulldate(new DateTime()));
        new_item.setUser_id("jsd2174");

        realm.beginTransaction();
        realm.copyToRealm(new_item);
        realm.commitTransaction();

        Record new_item1 = new Record();
        new_item.setRecord_id(System.currentTimeMillis()+2017);
        new_item.setUser_Lv(100);
        new_item.setUser_nickname("낭트");
        new_item.setTitleName("소나무");
        new_item.setUser_lyrics("두리번 두리번\\n누가 내 삶 가져갔어\\n전부 도둑놈 같아\\n매일 들이닥치는 자연재해\\n난 손도 못 쓰고 당해\\n일 더하고 일해 \\nBut they call me \\n 귀요미 Thang\\n내 랩이 어쩌고 저쩌고\\n나불대네 고맙다");
        new_item.setIsLikes(false); //
        new_item.setCount_likes(33); // 서버 업데이트

        new_item.setIsCustom(false);
        new_item.setRegister_datetime(getFulldate(new DateTime()));
        new_item.setUser_id("jsd2174");

        realm.beginTransaction();
        realm.copyToRealm(new_item1);
        realm.commitTransaction();
    }
    private int getFulldate(DateTime date){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        return Integer.parseInt(date.toString(fmt));
    }

    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(0);
                //ItemAdapter.showLoadingView();
            }
        }, 500);
    }

    private void startIntroAnimation() {
        fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        /*
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);
        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getIvLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        getInboxMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
                */
    }

    private void startContentAnimation() {
        fabCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }
}
