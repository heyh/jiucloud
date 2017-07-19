package sy.service;


import sy.model.po.UserDeviceRel;

import java.util.List;

/**
 * Created by Stomic on 15/12/24.
 */
public interface UserDeviceRelService {
    /**
     * 添加用户与终端设备关系
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public UserDeviceRel insertUserDeviceRel(UserDeviceRel userDeviceRel) throws Exception;

    /**
     * 获取用户设备关系
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRel(UserDeviceRel userDeviceRel) throws Exception;

    /**
     * 根据用户ID查询设备号
     * @param userId
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelByUserId(String userId) throws Exception;

    /**
     * 根据用户ID列表查询设备号
     * @param userDeviceRelList
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelListByUserIdList(List<UserDeviceRel> userDeviceRelList) throws Exception;

    /**
     * 根据用户注册ID查询
     * @param registrationId
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectUserDeviceRelByRegistrationID(String registrationId)throws Exception;

    /**
     * 更新用户设备关系UserId
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public int updateUserDeviceRelUserId(UserDeviceRel userDeviceRel) throws Exception;

    /**
     * 更新用户设备关系注册ID
     * @param userDeviceRel
     * @return
     * @throws Exception
     */
    public int updateUserDeviceRelRegistrationID(UserDeviceRel userDeviceRel) throws Exception;

    /**
     * 获取所有的用户设备关系
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> selectAllUserDeviceRel() throws Exception;

    /**
     * 根据注册ID获取所有用户ID
     * @param userDeviceRelList
     * @return
     * @throws Exception
     */
    public List<UserDeviceRel> getUserDeviceRelByRegistrationIds(List<UserDeviceRel> userDeviceRelList) throws Exception;
}
