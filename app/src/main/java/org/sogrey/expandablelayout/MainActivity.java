package org.sogrey.expandablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.sogrey.expandablelayout.views.ExpandableLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpandableLayout expandablelayout = findViewById(R.id.expandablelayout);
        expandablelayout.setIds(R.id.lyt_parent, R.id.lyt_expand_content, R.id.btn_expand);
        expandablelayout.setIsExpand(true);//设置true 默认展开，设置false或不设置默认收起隐藏
        expandablelayout.setOnExpandListenerListener(new ExpandableLayout.OnExpandListenerListener() {
            @Override
            public void post(boolean isExpand) {
                Toast.makeText(MainActivity.this, isExpand ? "展开了" : "收起了", Toast.LENGTH_LONG).show();
            }
        });
    }
}
