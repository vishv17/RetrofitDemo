package com.example.roomdatabase.retrofitdemo;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi
{
    @GET("posts")
    // TODO: 15-05-2020 to pass multiple parameters
    /*Call<List<Post>> getPosts(
            @Query("userId") Integer userId1,
            @Query("userId") Integer userId2,
            @Query("_sort") String sort,
            @Query("_order") String order
            );*/
    Call<List<Post>> getPosts(@Query("userId") Integer[] userID,
                              @Query("_sort") String sort,
                              @Query("_order") String order
                              );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String,String> parameters);

    @GET("post/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@Field("userId") int userId,@Field("title") String title,@Field("body") String text);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

    /**
     * PUT method replace whole object and we have to pass object as parameter
     * @param id
     * @param post
     * @return
     */
    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id,@Body Post post);

    /**
     * PATCH method will update only passed parameter in database
     * @param id
     * @param post
     * @return
     */
    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id,@Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
