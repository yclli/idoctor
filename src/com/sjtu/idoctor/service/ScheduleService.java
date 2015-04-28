package com.sjtu.idoctor.service;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.DocScheduleCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;

public interface ScheduleService {
	//TODO
	@GET(Url.URL_SCHEDULE_PLAN)
	HttpWrapper<DocScheduleCacheBean> getSchedulePlan(
			@Path("gid")int geroId,
			@Query("role")String role,
			@Query("page")String page,
			@Query("rows")String rows,
			@Query("sort")String sort,
			@Query("username")String username,
			@Query("digest")String digest
	);
}
