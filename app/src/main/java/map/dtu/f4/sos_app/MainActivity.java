package map.dtu.f4.sos_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import map.dtu.f4.sos_app.beans.Contact;
import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference databaseReference;
    ArrayList<String> listHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listHelper = new ArrayList<>();
        //khoi tao chanel neu chua co
        databaseReference.child("channel").child(Provider.me.getId()).child("status").setValue("ok");
        databaseReference.child("contacts").child(Provider.me.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Provider.listContact = new ArrayList<>();
                for (DataSnapshot contact : dataSnapshot.getChildren()){
                    String name = contact.child("name").getValue().toString();
                    String sdt = contact.child("phone").getValue().toString();
                    Provider.listContact.add(new Contact(name,sdt));
                    Log.d("aaaa",Provider.listContact.get(0).toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void sendSMS(String sdt){
        String messageToSend = "Tôi đang gặp nguy hiểm tại http://www.google.com/maps/place/"+Provider.me.getCoordinate().getLatitude()+","+Provider.me.getCoordinate().getLongitude();
        String number = sdt;
        SmsManager.getDefault().sendTextMessage(number,null,messageToSend,null,null);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void updateDangerLocationToServer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://xnam7799.000webhostapp.com/luu-hoat-dong", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.d("updatelocal","update success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updatelocal","update failed");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("updatelocal","location updating");
                Date d = new Date();
                HashMap<String, String> params = new HashMap<>();
                params.put("NguoiDung", Provider.me.getId());
                params.put("KinhDo", Provider.me.getCoordinate().getLatitude()+"");
                params.put("ViDo", Provider.me.getCoordinate().getLongitude()+"");
                params.put("ThoiGian", d.toString());
                params.put("NoiDung", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //su kien giu nut giam am luong
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            updateDangerLocationToServer();
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
                data.put("VictimMessage","Đang gặp nguy hiểm");
                data.put("Seen","false");
                databaseReference.child("channel").child(id).child("victims").child("v-"+me.getId()).setValue(data);
            }
            return true;
        }
        return onKeyLongPress(keyCode,event);
    }
}
