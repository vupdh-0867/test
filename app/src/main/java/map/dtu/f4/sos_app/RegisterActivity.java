package map.dtu.f4.sos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText txtCMND, txtHoVaTen, txtSDT, txtNgaySinh, txtEmail, txtMatKhau;
    Button btnDangKy, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://xnam7799.000webhostapp.com/DangKy.php";
                String cmnd = txtCMND.getText().toString();
                String hovaten = txtHoVaTen.getText().toString();
                String sdt = txtSDT.getText().toString();
                String ngaysinh = txtNgaySinh.getText().toString();
                String email = txtEmail.getText().toString();
                String matkhau = txtMatKhau.getText().toString();
                String hinhanh = "chua co";
                dangKy(url,cmnd,hovaten,sdt,ngaysinh,hinhanh,email,matkhau);
            }
        });
    }

    private  void anhXa(){
        txtCMND = (EditText) findViewById(R.id.txtCMND);
        txtHoVaTen = (EditText) findViewById(R.id.txtHoVaTen);
        txtSDT = (EditText) findViewById(R.id.txtSDT);
        txtNgaySinh = (EditText) findViewById(R.id.txtNgaySinh);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtMatKhau = (EditText) findViewById(R.id.txtPassword);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    private void dangKy(String url ,final String cmnd, final String hovaten, final String dienthoai, final String ngaysinh, final String hinhanh, final String email, final String matkhau){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Có lỗi xảy ra, kiểm tra lại thông tin!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("cmnd", cmnd);
                params.put("hovaten", hovaten);
                params.put("dienthoai", dienthoai);
                params.put("ngaysinh", ngaysinh);
                params.put("hinhanh", hinhanh);
                params.put("email", email);
                params.put("matkhau", matkhau);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
