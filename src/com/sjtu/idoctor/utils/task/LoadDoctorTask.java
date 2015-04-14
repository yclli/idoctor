package com.sjtu.idoctor.utils.task;

import java.util.List;

import javax.inject.Inject;

import com.sjtu.idoctor.utils.task.SafeAsyncTask;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.service.IdoctorService;

public class LoadDoctorTask extends SafeAsyncTask<List<DoctorCacheBean>>{
	@Inject
	protected IdoctorService idoctorService;
	private User user;
	private String digest;

//	public AreaTask(int areaId,Activity activity) {
//		// TODO Auto-generated constructor stub
//		this.areaId =areaId;
//		this.activity = activity;
//	}
	
	public LoadDoctorTask(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.digest = user.getDigest();
	}
	
	@Override
	public List<DoctorCacheBean> call() throws Exception {
		// TODO Auto-generated method stub
		idoctorService = new IdoctorService(digest, user);
		return idoctorService.getDoctors("医生");
	}
}
