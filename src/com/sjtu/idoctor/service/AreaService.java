package com.sjtu.idoctor.service;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.sjtu.idoctor.business.Url;
import com.sjtu.idoctor.model.AreaCacheBean;
import com.sjtu.idoctor.model.HttpWrapper;

public interface AreaService {
	//not use
    @GET(Url.URL_AREA)
    HttpWrapper<AreaCacheBean> getArea(
    		@Path("gid")int geroId,
    		@Path("aid")int areaId,
    		@Query("username")String username,
    		@Query("digest")String digest
    		);
    
    @GET(Url.URL_AREAS)
    HttpWrapper<AreaCacheBean> getAreas(
    		@Path("gid")int geroId,
    		@Query("level")int level,
    		@Query("parent_id")int parentId,
    		@Query("username")String username,
    		@Query("digest")String digest
    		);
}
