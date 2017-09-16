package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.ClockinginDaol;
import sy.model.Clockingin;
import sy.service.ClockinginServiceI;

import java.util.List;

/**
 * Created by heyh on 2017/9/16.
 */

@Service
public class ClockinginServiceImpl implements ClockinginServiceI {

    @Autowired
    private ClockinginDaol clockinginDao;

    @Override
    public Clockingin Clockingin(Clockingin clockingin) {
        clockinginDao.save(clockingin);
        return clockingin;
    }

    @Override
    public List<Clockingin> getClockingins(List<Integer> ugroup) {
        return null;
    }

    @Override
    public int approveClockingin(String approveState) {
        return 0;
    }
}
