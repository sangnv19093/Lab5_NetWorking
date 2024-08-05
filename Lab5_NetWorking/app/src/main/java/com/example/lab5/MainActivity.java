package com.example.lab5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText txt1, txt2, txt3;
    Button btnInsert, btnSelect;
    TextView tvKQ;
    Context context = this;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        tvKQ = findViewById(R.id.tvKQ);

        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(v -> {
            selectVolley();
        });

        btnInsert.setOnClickListener(v -> {
//            insertData(txt1, txt2, txt3, tvKQ);
//            selectData();
//            deleteData(txt1);
//            updateData(txt1, txt2, txt3, tvKQ);
        });

    }

    private void selectVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://hungnq28.000webhostapp.com/su2024/select.php";
        JsonObjectRequest request = new JsonObjectRequest(url, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    JSONArray array = res.getJSONArray("products");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject p = array.getJSONObject(i);
                        String MaSP = p.getString("MaSP");
                        String TenSP = p.getString("TenSP");
                        String MoTa = p.getString("MoTa");
                        strKQ += "MaSP: "+MaSP+"\n"+"TenSP: "+TenSP+"\n"+"MoTa: "+MoTa+"\n"+"\n";
                    }
                    tvKQ.setText(strKQ);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvKQ.setText(volleyError.getMessage());
            }
        });
        requestQueue.add(request);
    }

    private void insertData(EditText txt1, EditText txt2, EditText txt3, TextView tvKQ) {
        SanPham s = new SanPham(txt1.getText().toString(),
                                txt2.getText().toString(),
                                txt3.getText().toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/000/api_check/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsertSanPham insertSanPham = retrofit.create(InsertSanPham.class);
        Call<ResSanPham> call = insertSanPham.insertSanPham(s.getMaSP(),s.getTenSP(),s.getMoTa());
        call.enqueue(new Callback<ResSanPham>() {
            @Override
            public void onResponse(Call<ResSanPham> call, Response<ResSanPham> response) {
                ResSanPham res = response.body();
                tvKQ.setText(res.getMessage());
            }

            @Override
            public void onFailure(Call<ResSanPham> call, Throwable throwable) {
                tvKQ.setText(throwable.getMessage());
            }
        });
    }

    String strKQ = "";
    List<SanPham> ls;
    private void selectData () {
        strKQ = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/000/api_check/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InsertSanPham sanPham = retrofit.create(InsertSanPham.class);
        Call<ResSanPham> call = sanPham.selectSanPham();
        call.enqueue(new Callback<ResSanPham>() {
            @Override
            public void onResponse(Call<ResSanPham> call, Response<ResSanPham> response) {
                ResSanPham res = response.body();
                ls = new ArrayList<>(Arrays.asList(res.getSanPham()));
                for(SanPham p: ls){
                    strKQ += "MaSP"+p.getMaSP()+"; TenSP"+p.getTenSP()+"; MoTa"+p.getMoTa()+"\n";
                }
                tvKQ.setText(strKQ);
            }

            @Override
            public void onFailure(Call<ResSanPham> call, Throwable throwable) {
                tvKQ.setText(throwable.getMessage());
            }
        });
    }


    private void deleteData(EditText txt1) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/000/api_check/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsertSanPham delSanPham = retrofit.create(InsertSanPham.class);
        Call<ResSanPham> call = delSanPham.deleteSanPham(txt1.getText().toString());
        call.enqueue(new Callback<ResSanPham>() {
            @Override
            public void onResponse(Call<ResSanPham> call, Response<ResSanPham> response) {
                ResSanPham res = response.body();
                tvKQ.setText(res.getMessage());
            }

            @Override
            public void onFailure(Call<ResSanPham> call, Throwable throwable) {
                tvKQ.setText(throwable.getMessage());
            }
        });
    }

    private void updateData(EditText txt1, EditText txt2, EditText txt3, TextView tvKQ) {
        SanPham s = new SanPham(txt1.getText().toString(),
                txt2.getText().toString(),
                txt3.getText().toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/000/api_check/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsertSanPham updateSanPham = retrofit.create(InsertSanPham.class);
        Call<ResSanPham> call = updateSanPham.updateSanPham(s.getMaSP(),s.getTenSP(),s.getMoTa());
        call.enqueue(new Callback<ResSanPham>() {
            @Override
            public void onResponse(Call<ResSanPham> call, Response<ResSanPham> response) {
                ResSanPham res = response.body();
                tvKQ.setText(res.getMessage());
            }

            @Override
            public void onFailure(Call<ResSanPham> call, Throwable throwable) {
                tvKQ.setText(throwable.getMessage());
            }
        });
    }

}