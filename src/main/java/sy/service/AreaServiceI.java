package sy.service;

import sy.model.Area;

import java.util.List;

/**
 * Created by heyh on 16/2/16.
 */
public interface AreaServiceI {

    List<Area> getAreas(String citycode);
}
