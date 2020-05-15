package com.example.roomdatabase.retrofitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://jsonplaceholder.typicode.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        getPosts();
        getComments();
    }


    private void getPosts() {
        Map<String,String> parameter = new HashMap<>();
        parameter.put("userId","1");
        parameter.put("_sort","id");
        parameter.put("_order","desc");


//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,6},"id","desc");

        // TODO: 15-05-2020 Pass parameter as query map
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameter);
        // TODO: 15-05-2020 if you don't want to pass sort and order by then pass null it will ignored by retrofit
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(4,null,null);

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
}
