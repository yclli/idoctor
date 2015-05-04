package com.sjtu.idoctor.service;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjtu.idoctor.utils.HmacSHA256Utils;
import com.sjtu.idoctor.utils.RestAdapterRequestInterceptor;
import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.AreaCacheBean;
import com.sjtu.idoctor.model.BloodPressureRecordCacheBean;
import com.sjtu.idoctor.model.DocScheduleCacheBean;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.model.ElderCacheBean;
import com.sjtu.idoctor.model.HeartRateRecordCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;
import com.sjtu.idoctor.model.TemperatureCacheBean;
import com.sjtu.idoctor.model.BloodPressureCacheBean;
import com.sjtu.idoctor.model.HeartRateCacheBean;
import com.sjtu.idoctor.model.TemperatureRecordCacheBean;
import com.sjtu.idoctor.model.User;

/**
 * @author kang shiyong
 * @Date 2015/3/25
 */
public class IdoctorService {
	private RestAdapter restAdapter;
	private String digest;
	private int geroId;
	private String username;
	
	/**
     * Create icarer service
     * Default CTOR
     */
	public IdoctorService() {
		this.restAdapter = new RestAdapter.Builder()
										.setEndpoint(Url.URL_BASE)
										.setLogLevel(RestAdapter.LogLevel.FULL)
										.setRequestInterceptor(new RestAdapterRequestInterceptor())
										.setConverter(new GsonConverter(provideGson()))	
										.build();
	}
	
	public IdoctorService(String digest,User user) {
		this.restAdapter = new RestAdapter.Builder()
										.setEndpoint(Url.URL_BASE)
										.setLogLevel(RestAdapter.LogLevel.FULL)
										.setRequestInterceptor(new RestAdapterRequestInterceptor())
										.setConverter(new GsonConverter(provideGson()))		
										.build();
		this.digest = digest;
	    this.geroId = user.getGeroId();
	    this.username = user.getUsername();
	}
	
	/**
     * Create icarer service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
	@Inject
	public IdoctorService(RestAdapter restAdapter){
		this.restAdapter = restAdapter;
	}
	
	public IdoctorService(RestAdapter restAdapter,String digest,User user){
		this.restAdapter = restAdapter;
	    this.digest = digest;
	    this.geroId = user.getGeroId();
	    this.username = user.getUsername();
	}
	
	/*
	 * 
	 * Area Part
	 * 
	 */
	public AreaCacheBean getArea(int areaId){
		
		HttpWrapper<AreaCacheBean> model = getAreaService()
				.getArea(geroId, areaId, username, getDigestWithUsername());
		return model.getEntity();
	}
	
	public List<AreaCacheBean> getAreas(int level, int parentId){
		
		String digestValue = getDigest(level+""+parentId+username);
		HttpWrapper<AreaCacheBean> model = getAreaService()
				.getAreas(geroId, level, parentId, username, digestValue);
		return model.getEntities();
	}
	
	/*
	 * 
	 * User Part
	 * 
	 */
	
	public List<ElderCacheBean> getElderByArea(int areaId){
		String digestValue = getDigest(areaId+"120ID"+username);
		HttpWrapper<ElderCacheBean> model = getUserService().getElders(geroId, areaId,"1", "20","ID",username, digestValue);
		return model.getEntities();
	}
	
	public List<DoctorCacheBean> getDoctors(String role){
		String digestValue = getDigest("1"+role+"20ID"+username);
		HttpWrapper<DoctorCacheBean> model = getUserService().getDoctors(geroId, role, "1", "20", "ID", username, digestValue);
		return model.getEntities();
	}
	
	public List<DocScheduleCacheBean> getCurrentDoctor(String role){
		String digestValue = getDigest("1"+role+"20ID"+username);
		HttpWrapper<DocScheduleCacheBean> model = getScheduleService().getSchedulePlan(geroId, role, "1", "20", "ID", username, digestValue);
		return model.getEntities();
	}
	
	/*GET a User Object with digest key*/
	public User authenticate(String username, String password) {
		User authUser = new User(username, password);
		HttpWrapper<User> model = getUserService().userLogin(authUser);
		authUser = model.getEntity();
		return authUser;
    }
	
	/*
	 * 
	 * Health Part
	 * 
	 */
	public TemperatureRecordCacheBean getTemperature(int elderId, String startDate, String endDate){
		String digestValue = getDigest(endDate+startDate+username);
		HttpWrapper<TemperatureRecordCacheBean> model = getHealthService()
				.getTemperature(geroId, elderId, startDate, endDate, username, digestValue);
		return model.getEntity();
	}
	
	public BloodPressureRecordCacheBean gettBloodPressure(int elderId, String startDate, String endDate){
		String digestValue = getDigest(endDate+startDate+username);
		HttpWrapper<BloodPressureRecordCacheBean> model = getHealthService()
				.gettBloodPressure(geroId, elderId, startDate, endDate, username, digestValue);
		return model.getEntity();
	}
	
	public HeartRateRecordCacheBean getHeartRate(int elderId, String startDate, String endDate){
		String digestValue = getDigest(endDate+startDate+username);
		HttpWrapper<HeartRateRecordCacheBean> model = getHealthService()
				.getHeartRate(geroId, elderId, startDate, endDate, username, digestValue);
		return model.getEntity();
	}
	
	public boolean insertTemperature(int elderId, TemperatureCacheBean temprature){
		String digestValue = getDigestWithUsername();
		HttpWrapper<?> model = getHealthService()
				.insertTemperature(geroId, elderId, temprature, username, digestValue);
		if(model.isOk()){
			return true;
		}
		return false;
	} 
	
	public boolean insertBloodPressure(int elderId, BloodPressureCacheBean bloodPressure){
		String digestValue = getDigestWithUsername();
		HttpWrapper<?> model = getHealthService()
				.insertBloodPressure(geroId, elderId, bloodPressure, username, digestValue);
		if(model.isOk()){
			return true;
		}
		return false;
	}
	
	public boolean insertHeartRate(int elderId, HeartRateCacheBean heartRate){
		String digestValue = getDigestWithUsername();
		HttpWrapper<?> model = getHealthService()
				.insertHeartRate(geroId, elderId, heartRate, username, digestValue);
		if(model.isOk()){
			return true;
		}
		return false;
	}
	
	/*---------------------------------------------------------*/
	
	private RestAdapter getRestAdapter() {
        return restAdapter;
    }
	
	private AreaService getAreaService(){
		return getRestAdapter().create(AreaService.class);
	}
	
	private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }
	
	private HealthService getHealthService(){
		return getRestAdapter().create(HealthService.class);
	}
	
	private ScheduleService getScheduleService(){
		return getRestAdapter().create(ScheduleService.class);
	}
	
	@SuppressWarnings("unused")
	private String getDigest(Map<String, String[]> params){
		Map<String,Object> p = new HashMap<String,Object>(params);
		String[] usernameList = {username};
		p.put("username", usernameList);
		return HmacSHA256Utils.digest(this.digest, p);
	}
	
	/*
	 * @linedParams: param values joined in key's alphabet order
	 * */
	private String getDigest(String linedParams){
		
		return HmacSHA256Utils.digest(this.digest, linedParams);
	}
	
	private String getDigestWithUsername(){
//		Map<String,Object> p = new HashMap<String,Object>();
//		String[] usernameList = {username};
//		p.put("username", usernameList);
//		return HmacSHA256Utils.digest(this.digest, p);
		return getDigest(username);
	}
	
	Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
        		.setDateFormat("yyyy-MM-dd HH:mm:ss")
        		.setPrettyPrinting()
//        		.serializeNulls()
        		.create();
    }
}
