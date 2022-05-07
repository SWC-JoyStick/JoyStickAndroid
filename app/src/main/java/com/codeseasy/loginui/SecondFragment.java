package com.codeseasy.loginui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeseasy.loginui.cardView.Card;
import com.codeseasy.loginui.cardView.CardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondFragment extends Fragment  {

    private Context context;

    private CardAdapter adapter;

    private Card[] cards = {new Card("使命召唤WarZone即将上线", R.drawable.game), new Card("'震惊'GTA6制作计划已启动", R.drawable.game1),
            new Card("GreyMoor正在热议", R.drawable.game2), new Card("来领略大表哥中迷人风景", R.drawable.game3), new Card("孤独之路的故事", R.drawable.game4),
            new Card("新型载具已上线GTA5Online", R.drawable.game5), new Card("赛博朋克2077", R.drawable.game6),
            new Card("童年回忆像素风", R.drawable.game7), new Card("经典IP 哭泣6", R.drawable.game8), new Card("国产巨制侠客梦", R.drawable.game9),new Card("寂静雪野上的狙击手", R.drawable.game10),new Card("一起爆肝老头环", R.drawable.game11)};


    private List<Card> cardList = new ArrayList<>();
    public SecondFragment() {

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_fragment,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //在这里注册button

        //获取context
        context = getActivity();

        initCards();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_card);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);

    }

    private void initCards() {
        cardList.clear();
        for (int i = 0; i < cards.length; i++) {

            cardList.add(cards[i]);
        }
    }
}

