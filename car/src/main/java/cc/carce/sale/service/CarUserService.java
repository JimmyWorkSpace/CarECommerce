package cc.carce.sale.service;

import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.mapper.manager.CarUserMapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
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
        user.setNickName(nickName != null ? nickName : "用戶" + phoneNumber.substring(Math.max(0, phoneNumber.length() - 5)));
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

    /**
     * 使用密码登录
     * @param phoneNumber 手机号
     * @param password 密码（明文）
     * @return 用户实体，如果密码错误返回null
     */
    public CarUserEntity loginWithPassword(String phoneNumber, String password) {
        if (StrUtil.isBlank(phoneNumber) || StrUtil.isBlank(password)) {
            return null;
        }
        
        CarUserEntity user = findByPhoneNumber(phoneNumber);
        if (user == null) {
            log.warn("用户不存在: {}", phoneNumber);
            return null;
        }
        
        // 验证密码
        String encryptedPassword = encryptPassword(password);
        if (!encryptedPassword.equals(user.getPwd())) {
            log.warn("密码错误: 手机号={}", phoneNumber);
            return null;
        }
        
        // 更新最后登录时间
        updateLastLoginTime(user.getId());
        return user;
    }

    /**
     * 注册新用户或修改密码
     * @param phoneNumber 手机号
     * @param password 密码（明文）
     * @return 用户实体
     */
    public CarUserEntity registerOrUpdatePassword(String phoneNumber, String password) {
        if (StrUtil.isBlank(phoneNumber) || StrUtil.isBlank(password)) {
            return null;
        }
        
        CarUserEntity user = findByPhoneNumber(phoneNumber);
        String encryptedPassword = encryptPassword(password);
        
        if (user == null) {
            // 新用户注册
            user = createUser(phoneNumber, null);
            user.setPwd(encryptedPassword);
            carUserMapper.updateByPrimaryKeySelective(user);
            log.info("新用户注册成功: 手机号={}", phoneNumber);
        } else {
            // 修改密码
            user.setPwd(encryptedPassword);
            carUserMapper.updateByPrimaryKeySelective(user);
            log.info("用户密码修改成功: 手机号={}", phoneNumber);
        }
        
        return user;
    }

    /**
     * 加密密码（MD5，转大写）
     * @param password 明文密码
     * @return 加密后的密码（大写）
     */
    public String encryptPassword(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        return MD5.create().digestHex(password).toUpperCase();
    }

    /**
     * 检查手机号是否已注册
     * @param phoneNumber 手机号
     * @return true表示已注册，false表示未注册
     */
    public boolean isPhoneNumberRegistered(String phoneNumber) {
        if (StrUtil.isBlank(phoneNumber)) {
            return false;
        }
        CarUserEntity user = findByPhoneNumber(phoneNumber);
        return user != null;
    }
}
