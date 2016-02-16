package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.City;

import java.util.List;

@Repository
public interface CityServiceI {
	/**
	 * 根据省级信息获取市级列表信息
	 */
	List<City> getCities(String provincecode);
    City getCityByName(String cityName);

}
