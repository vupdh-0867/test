package map.dtu.f4.sos_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendHelping extends AppCompatActivity {
    String suCo = "";
    String trangThai = "";
    EditText loiNhan;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_helping);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    //tim xem checkbox nao duoc chon
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.CBSuCo1:
                if (checked) {
                    suCo = "Sự cố 1";
                    trangThai = "2";
                }
            else
                break;
            case R.id.CBSuCo2:
                if (checked) {
                    suCo = "Sự cố 2";
                    trangThai = "4";
                }
            else
                break;
            case R.id.CBSuCo3:
                if (checked) {
                    suCo = "Sự cố 3";
                    trangThai = "5";
                }
                else
                    break;
            // TODO: Veggie sandwich
        }
    }
    //su kien nut gui
    public void guiHelping(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfor", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","null");
        String name = sharedPreferences.getString("name","null");
        loiNhan = (EditText) findViewById(R.id.editTextMsg);
        databaseReference.child("UserLocation").child(id).child("id").setValue(id);
        databaseReference.child("UserLocation").child(id).child("name").setValue(name);
        databaseReference.child("UserLocation").child(id).child("status").setValue(trangThai);
        databaseReference.child("UserLocation").child(id).child("message").setValue(loiNhan.getText().toString());
        Toast.makeText(this,"Gửi thành công",Toast.LENGTH_LONG).show();
    }
    //su kien nut huy
    public void huyGuiHelping(View v){
        Toast.makeText(this,"Đã hủy gửi",Toast.LENGTH_LONG).show();
    }
}
