package sy.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import sy.model.S_province;

@Repository
public interface ProvinceServiceI {
	/**
	 * 获取全部省级列表信息
	 */
	List<S_province> getProvinces();

	S_province getProvinceByName(String name);
}
