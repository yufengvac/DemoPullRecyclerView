package com.yefoo.demopullrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.interfaces.OnLoadMoreClickListener;
import com.aspsine.irecyclerview.interfaces.OnLoadMoreListener;
import com.aspsine.irecyclerview.interfaces.OnRefreshListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

//    private PullToRefreshRecycleView pullToRefreshRecycleView;
    private IRecyclerView recyclerView;
    private MyAdapter myAdapter;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        pullToRefreshRecycleView = findViewById(R.id.pull_recycler_view);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setAutoLoadMore(true);
        recyclerView.setOnLoadMoreClickListener(new OnLoadMoreClickListener() {
            @Override
            public void onLoadMoreClick() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                    }
                },500);
            }
        });
        myAdapter = new MyAdapter();
        recyclerView.setIAdapter(myAdapter);

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                },500);
            }
        });

        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                    }
                },500);
            }
        });

        initData();
    }

    private void initData(){
        List<Student> list = new ArrayList<>();
        index = 1;
        for (int i= index; i < index + 20; i++){
            Student student = new Student();
            student.setId(i);
            student.setName(String.valueOf(getRandomChar())+String.valueOf(getRandomChar()));
            list.add(student);
        }
        myAdapter.setData(list);
        recyclerView.setRefreshing(false);
        index = index + 20;

//        pullToRefreshRecycleView.completeRefresh();
    }

    private void loadMoreData(){
        if (index > 60){
            recyclerView.setLoadMoreState(null);
        }else {
            List<Student> list = new ArrayList<>();
            for (int i= index; i < index + 20; i++){
                Student student = new Student();
                student.setId(i);
                student.setName(String.valueOf(getRandomChar())+String.valueOf(getRandomChar()));
                list.add(student);
            }
            myAdapter.addData(list);
            index = index + 20;
            recyclerView.setLoadMoreComplete();
        }
//        pullToRefreshRecycleView.completeLoadMore();
    }

    private char getRandomChar() {
        String str = "";
        int highPos;
        int lowPos;

        Random random = new Random();

        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

}
