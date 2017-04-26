package news.example.zdw.serporttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MsgCallback {

    private EditText mEtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtData= (EditText) findViewById(R.id.et_data);
        new SerPortUtil().setMsgCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void OnReceive(String data) {
        //接收的数据
        mEtData.setText(data);
    }

    @Override
    public void OnMessage(String Action) {

    }
}
