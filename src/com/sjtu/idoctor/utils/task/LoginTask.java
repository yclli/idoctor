package com.sjtu.idoctor.utils.task;

import javax.inject.Inject;

import retrofit.RestAdapter;

import android.app.Activity;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.service.IdoctorService;

public class LoginTask extends ProgressDialogTask<User> {
	@Inject
	protected IdoctorService idoctorService;
	String username;
	String password;
	User user;
	
	public LoginTask(Activity activity, String username, String password){
		super(activity);
		this.username = username;
		this.password = password;
	}
	
	public LoginTask start(){
		showIndeterminate(getString(R.string.message_login));
		execute();
		return this;
	}
	
	@Override
	public User call() throws Exception {
		// TODO Auto-generated method stub
		idoctorService = new IdoctorService();
		return idoctorService.authenticate(username, password);
		
	}
}
