package cc.carce.sale.service;

import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.mapper.manager.CarUserMapper;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class CarUserService {

    @Resource
    private CarUserMapper carUserMapper;

    /**
     * 根据手机号查找用户
     * @param phoneNumber 手机号
     * @return 用户实体
     */
    public CarUserEntity findByPhoneNumber(String phoneNumber) {
        if (StrUtil.isBlank(phoneNumber)) {
            return null;
        }
        
        Example example = new Example(CarUserEntity.class);
        example.createCriteria()
                .andEqualTo("phoneNumber", phoneNumber)
                .andEqualTo("delFlag", false);
        
        return carUserMapper.selectOneByExample(example);
    }

    /**
     * 创建新用户
     * @param phoneNumber 手机号
     * @param nickName 昵称
     * @return 用户实体
     */
    public CarUserEntity createUser(String phoneNumber, String nickName) {
        CarUserEntity user = new CarUserEntity();
        user.setPhoneNumber(phoneNumber);
        user.setNickName(nickName != null ? nickName : "用户" + phoneNumber.substring(7));
        user.setDelFlag(false);
        user.setCreateTime(new Date());
        user.setLastLoginTime(new Date());
        
        carUserMapper.insert(user);
        log.info("创建新用户: {}", user);
        return user;
    }

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     */
    public void updateLastLoginTime(Long userId) {
        CarUserEntity user = new CarUserEntity();
        user.setId(userId);
        user.setLastLoginTime(new Date());
        
        carUserMapper.updateByPrimaryKeySelective(user);
        log.info("更新用户最后登录时间: {}", userId);
    }

    /**
     * 用户登录或注册
     * @param phoneNumber 手机号
     * @return 用户实体
     */
    public CarUserEntity loginOrRegister(String phoneNumber) {
        // 先查找用户是否存在
        CarUserEntity user = findByPhoneNumber(phoneNumber);
        
        if (user == null) {
            // 用户不存在，创建新用户
            user = createUser(phoneNumber, null);
        } else {
            // 用户存在，更新最后登录时间
            updateLastLoginTime(user.getId());
        }
        
        return user;
    }
}
