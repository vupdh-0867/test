package map.dtu.f4.sos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import map.dtu.f4.sos_app.beans.Provider;

public class ReceiveSOSActivity extends AppCompatActivity {
    Button btnXem, btnHuy;
    ArrayList<LatLng> myList;
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_sos);
        myList = (ArrayList<LatLng>) getIntent().getSerializableExtra("VictimCoor");
        btnXem = (Button) findViewById(R.id.btnXem);
        btnHuy = (Button) findViewById(R.id.btnBoQua);
        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for(victim :Provider.listVictim){
//
//                }
                Intent intent = new Intent(ReceiveSOSActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
