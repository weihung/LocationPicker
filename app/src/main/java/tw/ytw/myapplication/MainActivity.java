package tw.ytw.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        Button.OnClickListener,
        LocationPickerFragment.OnCompleteListener {

    private LocationPickerFragment mLocationPickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationPickerFragment = new LocationPickerFragment();
        mLocationPickerFragment.init(LocationPickerFragment.MULTIPLE_CHOOSE_MODE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mLocationPickerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        v.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mLocationPickerFragment.onClick(v);
            }

            @Override
            public void onDoubleClick(View v) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show());
            }
        });
    }


    @Override
    public void onComplete() {
        String data = "厨房;客厅";
        mLocationPickerFragment.updateNavigationList(data);
    }
}
