package com.sjtu.idoctor.service;

import retrofit.http.Body;
//import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.TemperatureCacheBean;
import com.sjtu.idoctor.model.BloodPressureCacheBean;
import com.sjtu.idoctor.model.HeartRateCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;

public interface HealthService {
	//TODO

//   @GET(Url.URL_ELDER_TEMPERATURE)
//	HttpWrapper<TempratureCacheBean> getTemperature(
//			@Path("gid") int geroId,
//			@Query("username")String username,
//			@Query("digest")String digest
//			);
    
    @POST(Url.URL_ELDER_TEMPERATURE)
    HttpWrapper<?> insertTemperature(
    		@Path("gid")int geroId,
    		@Path("eid")int elderId,
    		@Body TemperatureCacheBean temprature,
			@Query("username")String username,
			@Query("digest")String digest
			);
    
	@POST(Url.URL_ELDER_BLOODPRESSURE)
	HttpWrapper<?> insertBloodPressure(
			@Path("gid")int geroId,
			@Path("eid")int elderId,
			@Body BloodPressureCacheBean bloodPressure,
			@Query("username")String username,
			@Query("digest")String digest
			);
    
	@POST(Url.URL_ELDER_HEARTRATE)
	HttpWrapper<?> insertHeartRate(
			@Path("gid")int geroId,
			@Path("eid")int elderId,
			@Body HeartRateCacheBean heartRate,
			@Query("username")String username,
			@Query("digest")String digest
			);
}
