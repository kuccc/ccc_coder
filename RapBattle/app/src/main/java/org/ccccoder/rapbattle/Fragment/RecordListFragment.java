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
        mAdapter = new ItemAdapter(getActivity().getApplicationContext(), null, models,null);
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
        new_item.setUser_lyrics("잃어버린 나의 예배 맞닥뜨린 나의 한계 정상을 향해 쉴새없이 뛰어왔지만 돌아보면 난 여전히 제자리 걸음 마 날이갈수록 늘어나는 나의 설움만 커져가 주의 길을 가다보면 때론 비틀비틀 거리지만 조금 힘을 내 주를 향해 꿈틀꿈틀 거리기 시작해 그러나 다시금 시작되는 믿음의 불황 내 믿음 흔들릴 때 내 지각은 위기의 맨틀 오늘도 한 입 베어먹지 witch's apple 뜨거운 햇볕이 나를 데피듯 점점 지쳐가 나를 사로잡네 죄로 가득한 영겊 하지만 오늘도 난 주를 향해 go up");
        new_item.setIsLikes(false); //
        new_item.setCount_likes(33); // 서버 업데이트

        new_item.setIsCustom(false);
        new_item.setRegister_datetime(getFulldate(new DateTime()));
        new_item.setUser_id("jsd2174");

        realm.beginTransaction();
        realm.copyToRealm(new_item);
        realm.commitTransaction();

        Record new_item1 = new Record();
        new_item1.setRecord_id(System.currentTimeMillis()+2027);
        new_item1.setUser_Lv(100);
        new_item1.setUser_nickname("낭트");
        new_item1.setTitleName("소나무");
        new_item1.setUser_lyrics("벌스 쓰기 귀찮아서 그냥 프리스타일로 뭐 이정도만 해도 mc들 다 비탈로 보내버리는 래퍼 충족하게 spit 해 나 때문에 다른 mc들 핏대, 올라가 난 young generation like 지코 딘 내 랩은 너무 잘 빨려 마치 니코틴 내가 보기엔 여기 있는 래퍼들은 귀요미 랩 대신 딴거해 니코니코니 에이 주노플로우 빨리 도망가는게 좋아 나는 너를 잡는 추노플로우 면도는 면도하다가 베이고 빅원? 스몰 사이즈라고 내가 랩하면 비기 스몰즈 난, 벌스 안써도 뭐 이정도는 할수있다고 쇼미더머니 정형돈이라고 할수있어 왜냐면 난 gettin bigger 다른 사람들 다 비켜");
        new_item1.setIsLikes(false); //
        new_item1.setCount_likes(33); // 서버 업데이트

        new_item1.setIsCustom(false);
        new_item1.setRegister_datetime(getFulldate(new DateTime()));
        new_item1.setUser_id("jsd2174@naver.com");

        realm.beginTransaction();
        realm.copyToRealm(new_item1);
        realm.commitTransaction();

        Record new_item2 = new Record();
        new_item2.setRecord_id(System.currentTimeMillis()+2014);
        new_item2.setUser_Lv(54);
        new_item2.setUser_nickname("우람한 어깨");
        new_item2.setTitleName("멋있는 시");
        new_item2.setUser_lyrics("이 한 세상 살아가면서 슬픔은 모두 내가 가질테니 당신은 기쁨만 가지십시오 고통과 힘겨움은 내가 가질테니 당신은 즐거움만 가지십시오 줄 것만 있으면 나는 행복하겠습니다 더 바랄 게 없겠습니다");
        new_item2.setIsLikes(false); //
        new_item2.setCount_likes(24); // 서버 업데이트

        new_item2.setIsCustom(false);
        new_item2.setRegister_datetime(getFulldate(new DateTime()));
        new_item2.setUser_id("jaewoo940620@naver.com");

        realm.beginTransaction();
        realm.copyToRealm(new_item2);
        realm.commitTransaction();

        Record new_item3 = new Record();
        new_item3.setRecord_id(System.currentTimeMillis()+2018);
        new_item3.setUser_Lv(144);
        new_item3.setUser_nickname("우람한 어깨");
        new_item3.setTitleName("오호라");
        new_item3.setUser_lyrics("ㅃ2 괜츈아 (괜찮아 라는 뜻입니까?) 헉 떙쓰 뻥 뽀뽀 뿅뿅 프프프 크푸헤 읭? 따따따빵 꾸 오호라 우웨 풉풉풉 퍽퍽퍽 쳇 빠샤빠샤 땡큐 ㅇㅇ이이 흥흥 끼아악 앙/엉 뜨는디 캴캴캴 으흣  흐크크 으힛 음하하 훗훗훗 쿠흐흐");
        new_item3.setIsLikes(false); //
        new_item3.setCount_likes(3); // 서버 업데이트

        new_item3.setIsCustom(false);
        new_item3.setRegister_datetime(getFulldate(new DateTime()));
        new_item3.setUser_id("ahnpersie@gmail.com");

        realm.beginTransaction();
        realm.copyToRealm(new_item3);
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
