package com.sjtu.idoctor.view;

import java.io.UnsupportedEncodingException;
import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.utils.task.LoginTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private SharedPreferences preferences = null;
	private SharedPreferences.Editor editor = null;
	private EditText textname = null;
	private EditText textpassword = null;
	private Button button = null;
	boolean isLogin = false;
	boolean result=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		preferences = getSharedPreferences("Doctor",Context.MODE_PRIVATE);
		isLogin = preferences.getBoolean("isLogin", false);
		if(!isLogin){
			setContentView(R.layout.activity_login);
			
			textname = (EditText)findViewById(R.id.name);
		    textpassword = (EditText)findViewById(R.id.password);
		    button = (Button)findViewById(R.id.button);
		      
		    button.setOnClickListener(new mybuttonlistener());
		}
		else{
			startActivity(new Intent(this, DoctorActivity.class));
			finish();
		}
	}
	
	 class mybuttonlistener implements OnClickListener{
		    String name;
		    String password;
		    public void onClick(View v) {
		      try {        
		        name = textname.getText().toString();
		        name = new String(name.getBytes("ISO8859-1"), "UTF-8");
		        password = textpassword.getText().toString();
		        password = new String(password.getBytes("ISO8859-1"), "UTF-8");
		      } catch (UnsupportedEncodingException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		      }
		      try {
		    	login(name,password);
		      } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }
		    }
		  }
	 
	public void login(String username, String password){
		new LoginTask(this, username, password){
			@Override
            protected void onSuccess(User user) throws Exception {
                super.onSuccess(user);
                editor = preferences.edit();
		        editor.putBoolean("isLogin", true);
		        editor.putInt("id", user.getId());
		        editor.putInt("geroId", user.getGeroId());
		        editor.putString("username", user.getUsername());
		        editor.putString("digest", user.getDigest());
		        editor.commit();
                Log.d("idoctor.service","login success");
                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
		        finish();
            }
			@Override
    		protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                e.printStackTrace();
                //TODO
			}
		}.start();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
