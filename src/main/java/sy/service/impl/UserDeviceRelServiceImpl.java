package sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sy.dao.UserDeviceRelDaol;
import sy.model.po.UserDeviceRel;
import sy.service.UserDeviceRelService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userDeviceRelService")
public class UserDeviceRelServiceImpl implements UserDeviceRelService {

    @Autowired
    private UserDeviceRelDaol userDeviceRelDao;

    /**
     * 添加用户与终端设备关系
     *
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public UserDeviceRel insertUserDeviceRel(UserDeviceRel userDeviceRel) throws Exception {
        userDeviceRel.setCreateTime(new Date());
        userDeviceRel.setUpdateTime(new Date());

        userDeviceRelDao.save(userDeviceRel);

        return userDeviceRel;
    }

    /**
     * 获取用户设备关系
     *
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRel(UserDeviceRel userDeviceRel) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("REGISTRATION_ID", userDeviceRel.getRegistrationId());
        paramMap.put("USER_ID", StringUtils.isEmpty(userDeviceRel.getUserId()) ? "" : userDeviceRel.getUserId());

        String hql = "from UserDeviceRel a where a.registrationId=:REGISTRATION_ID and a.userId=:USER_ID";
        return userDeviceRelDao.find(hql, paramMap);
    }

    /**
     * 根据用户ID查询设备号
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelByUserId(String userId) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("USER_ID", StringUtils.isEmpty(userId) ? "" : userId);
        String hql = "from UserDeviceRel a where a.userId=:USER_ID";
        return userDeviceRelDao.find(hql, paramMap);
    }

    /**
     * 根据用户ID列表查询设备号
     *
     * @param userDeviceRelList
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelListByUserIdList(List<UserDeviceRel> userDeviceRelList) throws Exception {
        if (null == userDeviceRelList || userDeviceRelList.size() == 0)
            return null;
        String userDeviceListStr = "";
        for (UserDeviceRel userDeviceRel : userDeviceRelList) {
            userDeviceListStr = userDeviceListStr + userDeviceRel.getUserId() + ",";
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("USER_IDS", userDeviceListStr.substring(0, userDeviceListStr.length() - 1));
        String hql = "select * from UserDeviceRel a where a.userId IN :USER_IDS and a.is_push = '1' ";
        return userDeviceRelDao.find(hql, paramMap);
    }

    /**
     * 根据用户注册ID查询
     *
     * @param registrationId
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelByRegistrationID(String registrationId) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("REGISTRATION_ID", registrationId);
        String hql = " from UserDeviceRel a where a.registrationId=:REGISTRATION_ID";
        return userDeviceRelDao.find(hql, paramMap);
    }

    @Override
    public int updateUserDeviceRelUserId(UserDeviceRel userDeviceRel) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("USER_ID", StringUtils.isEmpty(userDeviceRel.getUserId()) ? "" : userDeviceRel.getUserId());
        paramMap.put("REGISTRATION_ID", userDeviceRel.getRegistrationId());
        String sql = " UPDATE UserDeviceRel set userId = :USER_ID where registrationId =:REGISTRATION_ID ";
        return userDeviceRelDao.executeSql(sql, paramMap);
    }

    @Override
    public int updateUserDeviceRelRegistrationID(UserDeviceRel userDeviceRel) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("USER_ID", StringUtils.isEmpty(userDeviceRel.getUserId()) ? "" : userDeviceRel.getUserId());
        paramMap.put("REGISTRATION_ID", userDeviceRel.getRegistrationId());
        String sql = " UPDATE UserDeviceRel set registrationId =:REGISTRATION_ID where userId = :USER_ID ";
        return userDeviceRelDao.executeSql(sql, paramMap);
    }

    /**
     * 获取所有的用户设备关系
     * @return
     * @throws Exception
     */
    @Override
    public List<UserDeviceRel> selectAllUserDeviceRel() throws Exception {
        String hql = "from UserDeviceRel a where a.isPush = '1'";
        return userDeviceRelDao.find(hql);
    }

    /**
     * 根据注册ID获取用户ID列表
     *
     * @param userDeviceRelList
     * @return
     * @throws Exception
     */
    @Override
    public List<UserDeviceRel> getUserDeviceRelByRegistrationIds(List<UserDeviceRel> userDeviceRelList) throws Exception {
        if (null == userDeviceRelList || userDeviceRelList.size() == 0) {
            return null;
        }

        String userDeviceListStr = "";
        for (UserDeviceRel userDeviceRel : userDeviceRelList) {
            if (!StringUtils.isEmpty(userDeviceRel.getRegistrationId())) {
                userDeviceListStr = userDeviceListStr + userDeviceRel.getRegistrationId() + ",";
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("REGISTRATION_IDS", userDeviceListStr.substring(0, userDeviceListStr.length() - 1));
        String hql = "from UserDeviceRel a where a.registrationId IN :REGISTRATION_IDS and a.is_push = '1'";
        return userDeviceRelDao.find(hql, paramMap);
    }
}
