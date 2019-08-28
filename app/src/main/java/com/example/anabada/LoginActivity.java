package com.example.anabada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    String loginname;
    Boolean logout=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final SharedPreferences auto=getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginname=auto.getString("inputname",null);

        if (loginname!=null){
            Toast.makeText(getApplicationContext(), loginname +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent);
        }

        Button gotomainButton = (Button) findViewById(R.id.gotomainButton);
        gotomainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = idText.getText().toString();

                //Lister에서 원하는 결과값 다룰수 있게
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인 완료!")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                            }

                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인 실패!")
                                        .setNegativeButton("확인",null)
                                        .create()
                                        .show();

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };
                if (!TextUtils.isEmpty(idText.getText())){
                    SharedPreferences.Editor autoLogin=auto.edit();
                    autoLogin.putString("inputname",idText.getText().toString());
                    autoLogin.commit();

                    RegisterRequest userRegisterRequest = new RegisterRequest(userName,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(userRegisterRequest); //버튼 클릭시 roomRegisterRequest 실행
                }
                else{
                    Toast.makeText(getApplicationContext(),"이름이 필요합니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}