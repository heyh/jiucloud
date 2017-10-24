package sy.service;

import org.springframework.stereotype.Service;
import sy.model.po.Location;

import java.util.List;

/**
 * Created by heyh on 2017/8/3.
 */

public interface LocationServiceI {

    List<Location> getLocations(String cid, List<Integer> ugroup, String keyword);

    List<Location> getLocations(String cid, String keyword);

    List<Location> getLocationsByName(String cid, String mc);

    Location addLocation(String cid, String uid, String mc);

    void delLocation(String id);
}
