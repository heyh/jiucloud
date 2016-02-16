package sy.service;

import org.springframework.stereotype.Repository;
import sy.model.Province;

import java.util.List;

@Repository
public interface ProvinceServiceI {
	/**
	 * 获取全部省级列表信息
	 */
	List<Province> getProvinces();

	Province getProvinceByName(String name);
}
