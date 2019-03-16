package map.dtu.f4.sos_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import map.dtu.f4.sos_app.beans.Contact;
import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.StringProcess;

public class AddContactActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRcvAdapter;
    Button btnAddContact;
    DatabaseReference databaseReference;
    Dialog dialog;
    Map<String,String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        //load du lieu contact tu db len recycler view
        loadContact(Provider.listContact);
        //tao su kien cho nut them moi
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //btnAddContact = (Button) findViewById(R.id.btnAddContact);

    }
    public void showAddContact(View v){
        dialog = new Dialog(AddContactActivity.this);
        dialog.setContentView(R.layout.edit_contact_layout);
        dialog.show();
    }
    public void cancelAddContact(View view){
        dialog.dismiss();
    }
    public void addContact(View v){
        Log.d("aaaaaa",""+Provider.listContact.size());
        EditText txtSDT = (EditText) dialog.findViewById(R.id.ecSDT);
        EditText txtTen = (EditText) dialog.findViewById(R.id.ecName);
        if(txtSDT.getText().toString().trim().isEmpty() || txtTen.getText().toString().trim().isEmpty()){
            Toast.makeText(AddContactActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        if(txtSDT.getText().toString().length() > 11 || txtSDT.getText().toString().length() < 10 || StringProcess.notVaildNumber(txtSDT.getText().toString())){
            Toast.makeText(AddContactActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
        else {
            data = new HashMap<>();
            data.put("name", txtTen.getText().toString());
            data.put("phone", txtSDT.getText().toString());
            try {
                databaseReference.child("contacts").child(Provider.me.getId()).child(txtSDT.getText().toString()).setValue(data);
                Provider.listContact.add(new Contact(txtTen.getText().toString(), txtSDT.getText().toString()));
                Toast.makeText(AddContactActivity.this, "Thêm liên hệ thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(AddContactActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }

            loadContact(Provider.listContact);
            dialog.dismiss();
        }
    }

    private void loadContact(List<Contact> data){
        mRecyclerView = (RecyclerView) findViewById(R.id.listcontact);
        mRcvAdapter = new RecyclerViewAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRcvAdapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

        private List<Contact> data;
        private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        public RecyclerViewAdapter(List<Contact> data) {
             this.data = new ArrayList<>();
            this.data = data;
        }
        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.contact_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
            holder.txtName.setText(data.get(position).getTen());
            holder.txtSDT.setText(data.get(position).getSdt());
            //tao su kien sua thong tin khi nhan va giu
            holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //tao dialog de sua thong tin
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                    //tao mot lay out de set co dialog
                    LinearLayout linearLayout = new LinearLayout(AddContactActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText tvName = new EditText(AddContactActivity.this);
                    final EditText tvSDT = new EditText(AddContactActivity.this);
                    tvName.setText(holder.txtName.getText().toString());
                    tvSDT.setText(holder.txtSDT.getText().toString());
                    linearLayout.addView(tvName);
                    linearLayout.addView(tvSDT);
                    builder.setTitle("            Cập nhật thông tin");
                    builder.setView(linearLayout);
                    //cac nut sua va xoa
                    builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("contacts").child(Provider.me.getId()).child(holder.txtSDT.getText().toString()).removeValue();
                            holder.txtName.setText(tvName.getText().toString());
                            holder.txtSDT.setText(tvSDT.getText().toString());
                            Map<String,String> data1 = new HashMap<>();
                            data1.put("name",holder.txtName.getText().toString());
                            data1.put("phone",holder.txtSDT.getText().toString());
                            databaseReference.child("contacts").child(Provider.me.getId()).child(holder.txtSDT.getText().toString()).setValue(data1);
                            data.get(position).setSdt(holder.txtSDT.getText().toString());
                            data.get(position).setTen(holder.txtName.getText().toString());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            data.remove(position);
                            databaseReference.child("contacts").child(Provider.me.getId()).child(tvSDT.getText().toString()).removeValue();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;
            TextView txtSDT;
            ConstraintLayout constraintLayout;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                txtSDT = (TextView) itemView.findViewById(R.id.sdt);
                txtName = (TextView) itemView.findViewById(R.id.ten);
                constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.contactid);
            }
        }
    }
}
