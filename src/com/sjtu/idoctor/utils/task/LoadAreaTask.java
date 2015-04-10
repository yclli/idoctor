package com.sjtu.idoctor.utils.task;

import java.util.List;

import javax.inject.Inject;

import android.app.Activity;
import android.os.Handler;

import com.sjtu.idoctor.utils.task.SafeAsyncTask;
import com.sjtu.idoctor.model.AreaCacheBean;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.service.IdoctorService;

public class LoadAreaTask extends SafeAsyncTask<List<AreaCacheBean>>{
	@Inject
	protected IdoctorService idoctorService;
	private int level;
	private int parentId;
	private User user;
	private String digest;
	
	Activity activity;
//	public AreaTask(int areaId,Activity activity) {
//		// TODO Auto-generated constructor stub
//		this.areaId =areaId;
//		this.activity = activity;
//	}
	
	public LoadAreaTask(int level,int parentId, User user, Activity activity, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.level = level;
		this.parentId = parentId;
		this.user = user;
		this.digest = user.getDigest();
		this.activity = activity;
	}
	
	@Override
	public List<AreaCacheBean> call() throws Exception {
		// TODO Auto-generated method stub
		idoctorService = new IdoctorService(digest, user);
		return idoctorService.getAreas(level, parentId);
	}
}
