package sy.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import sy.model.S_city;

@Repository
public interface CityServiceI {
	/**
	 * 根据省级信息获取市级列表信息
	 */
	List<S_city> getCities(int province_name);

}
