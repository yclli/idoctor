package com.sjtu.idoctor.service;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.model.ElderCacheBean;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;

public interface UserService {
	/*admin获取当前用户*/
	@GET(Url.URL_USER)
	User getUser(@Path("uid")int userId);
	
	@GET(Url.URL_USER_ELDERS)
	HttpWrapper<ElderCacheBean> getElders(
			@Path("gid")int geroId, 
			@Query("area_id")int areaId,
			@Query("page")String page,
			@Query("rows")String rows,
			@Query("sort")String sort,
			@Query("username")String username,
			@Query("digest") String digest
			);
	
	@GET(Url.URL_STAFFS)
	HttpWrapper<DoctorCacheBean> getDoctors(
			@Path("gid")int geroId,
			@Query("role")String role,
			@Query("page")String page,
			@Query("rows")String rows,
			@Query("sort")String sort,
			@Query("username")String username,
			@Query("digest")String digest
			);
	
	@POST(Url.URL_USER_KEY)
    HttpWrapper<User> userLogin(@Body User user);
	
}
