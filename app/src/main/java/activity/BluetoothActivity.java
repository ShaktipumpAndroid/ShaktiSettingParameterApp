package activity;


import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.shakti.shaktinewconcept.R;

public class BluetoothActivity extends AppCompatActivity {


    private Context mContext;
    private RecyclerView rclvListViewID ;
    private RelativeLayout rlvFooterID1, rlvFooterID2, rlvFooterID3, rlvFooterID4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext =  this;
        initView();
    }

    private void initView() {
        rclvListViewID = (RecyclerView) findViewById(R.id.rclvListViewID);

        rclvListViewID.setHasFixedSize(true);
        rclvListViewID.setLayoutManager(new LinearLayoutManager(this));

        //creating recyclerview adapter
        SettingAdapter adapter = new SettingAdapter(mContext);

        //setting adapter to recyclerview
        rclvListViewID.setAdapter(adapter);

    }
}
