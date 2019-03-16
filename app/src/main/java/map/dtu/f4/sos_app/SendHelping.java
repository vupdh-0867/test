package map.dtu.f4.sos_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.User;

public class SendHelping extends AppCompatActivity {
    String suCo = "";
    String trangThai = "";
    EditText loiNhan;
    ArrayList<String> listHelper ;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_helping);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loiNhan = (EditText) findViewById(R.id.editTextMsg);
        listHelper = new ArrayList<>();
    }
    //tim xem checkbox nao duoc chon
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.CBSuCo1:
                if (checked) {
                    suCo = "Tai nạn giao thông";
                    trangThai = "2";
                }
            else
                break;
            case R.id.CBSuCo2:
                if (checked) {
                    suCo = "Cướp chặn đường";
                    trangThai = "4";
                }
            else
                break;
            case R.id.CBSuCo3:
                if (checked) {
                    suCo = "Hư hỏng xe cộ";
                    trangThai = "5";
                }
                else
                    break;
            // TODO: Veggie sandwich
        }
    }
    //su kien nut gui
    public void guiHelping(View v){
        databaseReference.child("UserLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User me = Provider.me;
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    //tim nguoi xung quanh 100m
                    if (user.child("coordinate").child("latitude").getValue() != null && user.child("coordinate").child("longitude").getValue()!=null){
                        String id = user.getKey().toString();
                        double latitude = Double.parseDouble(user.child("coordinate").child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(user.child("coordinate").child("longitude").getValue().toString());
                        double distance = Math.sqrt(Math.pow(me.getCoordinate().getLatitude() - latitude, 2) + Math.pow(me.getCoordinate().getLongitude() - longitude, 2));
                        Log.d("vuPhan", distance + "");
                        //neu khoang cach < 100 va khong phai minh
                        if (distance < (4.5 * Math.pow(10, -3)) & !id.equals(me.getId())) {
                            listHelper.add(id);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("updatelocal","location updating");
        for(String id : listHelper){
            User me = Provider.me;
            Map data = new HashMap<>();
            //databaseReference.child("channel").child(id).child("victims").child("v-"+me.getId()).child("").setValue();
            data.put("VictimId",me.getId());
            data.put("VictimName",me.getName());
            data.put("VicTimLatitude",me.getCoordinate().getLatitude());
            data.put("VicTimLongitude",me.getCoordinate().getLongitude());
            data.put("SendTime",me.getCoordinate().getTime());
            data.put("VictimMessage",loiNhan.getText().toString());
            data.put("Seen","false");
            databaseReference.child("channel").child(id).child("victims").child("v-"+me.getId()).setValue(data);
        }
    }
    //su kien nut huy
    public void huyGuiHelping(View v){
        Intent intent = new Intent(SendHelping.this,HomeActivity.class);
        startActivity(intent);
        Toast.makeText(this,"Đã hủy gửi",Toast.LENGTH_LONG).show();
    }
}
