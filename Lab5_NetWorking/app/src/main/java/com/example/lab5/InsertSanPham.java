package com.example.lab5;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InsertSanPham {
    @FormUrlEncoded
    @POST("insert.php")
    Call<ResSanPham> insertSanPham(
            @Field("MaSP") String MaSp,
            @Field("TenSP") String TenSP,
            @Field("MoTa") String MoTa
    );

    @GET("select.php")
    Call<ResSanPham> selectSanPham();

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResSanPham> deleteSanPham(
            @Field("MaSP") String MaSp
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResSanPham> updateSanPham(
            @Field("MaSP") String MaSp,
            @Field("TenSP") String TenSP,
            @Field("MoTa") String MoTa
    );

}
