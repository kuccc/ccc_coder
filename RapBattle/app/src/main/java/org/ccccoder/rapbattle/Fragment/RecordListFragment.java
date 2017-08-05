package org.ccccoder.rapbattle.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ccccoder.rapbattle.Adapter.ItemAdapter;
import org.ccccoder.rapbattle.Model.Record;
import org.ccccoder.rapbattle.Model.Title;
import org.ccccoder.rapbattle.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecordListFragment extends Fragment {
    ItemAdapter mAdapter;
    //List<ItemModel> models;
    RealmResults<Record> models;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Realm realm;  //DataBase
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_recordlist, container, false);
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        realm = Realm.getDefaultInstance();
        preset2();
        models = realm.where(Record.class).findAll();
        /*RecycleView 설정*/
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new ItemAdapter(getActivity().getApplicationContext(), null, models);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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
}
