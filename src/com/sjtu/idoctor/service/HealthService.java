package com.sjtu.idoctor.service;

import retrofit.http.Body;
import retrofit.http.GET;
//import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.BloodPressureRecordCacheBean;
import com.sjtu.idoctor.model.HeartRateRecordCacheBean;
import com.sjtu.idoctor.model.TemperatureCacheBean;
import com.sjtu.idoctor.model.BloodPressureCacheBean;
import com.sjtu.idoctor.model.HeartRateCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;
import com.sjtu.idoctor.model.TemperatureRecordCacheBean;

public interface HealthService {
	//TODO

	@GET(Url.URL_ELDER_TEMPERATURE)
	HttpWrapper<TemperatureRecordCacheBean> getTemperature(
			@Path("gid") int geroId,
			@Path("eid")int elderId,
			@Query("start_date")String startDate,
			@Query("end_date")String endDate,
			@Query("username")String username,
			@Query("digest")String digest
			);
	
	@GET(Url.URL_ELDER_BLOODPRESSURE)
	HttpWrapper<BloodPressureRecordCacheBean> gettBloodPressure(
			@Path("gid") int geroId,
			@Path("eid")int elderId,
			@Query("start_date")String startDate,
			@Query("end_date")String endDate,
			@Query("username")String username,
			@Query("digest")String digest
			);
	
	@GET(Url.URL_ELDER_HEARTRATE)
	HttpWrapper<HeartRateRecordCacheBean> getHeartRate(
			@Path("gid") int geroId,
			@Path("eid")int elderId,
			@Query("start_date")String startDate,
			@Query("end_date")String endDate,
			@Query("username")String username,
			@Query("digest")String digest
			);
    
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
