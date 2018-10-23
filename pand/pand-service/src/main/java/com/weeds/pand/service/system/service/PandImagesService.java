package com.weeds.pand.service.system.service;

import java.util.List;
import java.util.Map;

import com.weeds.pand.service.system.domain.PandImages;

public interface PandImagesService {
	
	List<PandImages> listPandImages(Map<String, Object> parameters);
	
	void savePandImages(int imageCardType,int imgModel,String modelId,List<String> imagesList);
	
	void deletePandImages(String id);

}
