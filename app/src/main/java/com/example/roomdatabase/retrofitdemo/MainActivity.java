package com.example.roomdatabase.retrofitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.txt_view_result);

        /**
         * This will Serialize null and tell server to delete the property in different requests
         * like put,patch and others
         */
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
         OkHttpClient okHttpClient = new OkHttpClient.Builder()
                 .addInterceptor(httpLoggingInterceptor)
                 .build();

        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://jsonplaceholder.typicode.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .client(okHttpClient)
                                    .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        getPosts();
//        getComments();
//        createPost();
        updatePost();
//        deletePost();
    }

    private void createPost() {
        Post post = new Post(23,"New Title","New text");
//        Call<Post> call = jsonPlaceHolderApi.createPost(post);
//        Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New text");
        Map<String, String> fields = new HashMap<>();
        fields.put("userId","25");
        fields.put("title","New Title");

        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful())
                {
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                Post responsePost = response.body();

                String content = "";
                content+= "Code : " + response.code() + "\n";
                content+= "ID : " + responsePost.getId() + "\n";
                content+= "User ID : "+responsePost.getUserId() + "\n";
                content+= "Title : "+responsePost.getTitle() + "\n";
                content+= "Text : "+responsePost.getText()+"\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }


    private void getPosts() {
        Map<String,String> parameter = new HashMap<>();
        parameter.put("userId","1");
        parameter.put("_sort","id");
        parameter.put("_order","desc");


        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameter);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful())
                {
                    List<Post> posts = response.body();

                    for(Post post : posts)
                    {
                        String content = "";
                        content+= "ID : " + post.getId() + "\n";
                        content+= "User ID : "+post.getUserId() + "\n";
                        content+= "Title : "+post.getTitle() + "\n";
                        content+= "Text : "+post.getText()+"\n\n";

                        textViewResult.append(content);
                    }
                }
                else
                {
                    textViewResult.setText("Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments() {
        Call<List<Comment>> call = jsonPlaceHolderApi
                .getComments("posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful())
                {
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for(Comment comment : comments)
                {
                    String content = "";
                    content+= "ID : " + comment.getId() + "\n";
                    content+= "Post ID : "+comment.getPostId() + "\n";
                    content+= "Name : "+comment.getName() + "\n";
                    content+= "Email : "+comment.getEmail()+"\n";
                    content+= "Text : "+comment.getText()+"\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12,null,"New Text");

        Call<Post> call = jsonPlaceHolderApi.putPost(5,post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful())
                {
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                Post responsePost = response.body();

                String content = "";
                content+= "ID : " + responsePost.getId() + "\n";
                content+= "User ID : "+responsePost.getUserId() + "\n";
                content+= "Title : "+responsePost.getTitle() + "\n";
                content+= "Text : "+responsePost.getText()+"\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code : "+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
