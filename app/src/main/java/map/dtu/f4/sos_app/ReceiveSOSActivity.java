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

public class ReceiveSOSActivity extends AppCompatActivity {
    Button btnXem, btnHuy;
    ArrayList<LatLng> myList;
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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String myID = getIntent().getStringExtra("MyID");
                String victimID = getIntent().getStringExtra("VictimID");
                //databaseReference.child("InContact").setValue(victimID);
                databaseReference.child("InContact").child(victimID).child("HelperID").setValue(myID);
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("VictimCoor",myList);
                startActivity(intent);
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
