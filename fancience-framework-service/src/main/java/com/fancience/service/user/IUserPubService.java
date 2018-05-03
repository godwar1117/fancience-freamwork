package com.fancience.service.user;

import com.fancience.api.exception.ServiceException;
import com.fancience.uc.entity.vo.UserInfoVO;

import java.util.List;

/**
 * 用户数据同步接口
 * Created by Leonid on 18/4/24.
 */
public interface IUserPubService {
    /**
     * 实时通过fancienceId 获取 UserInfoVO list
     * @param fancienceId
     * @return
     * @throws ServiceException
     */
    List<UserInfoVO> listUserInfoVORealTime(List<Long> fancienceId) throws ServiceException;

    /**
     * 实时通过fancienceId 获取 UserInfoVO list
     * @param fancienceId
     * @return
     * @throws ServiceException
     */
    UserInfoVO getUserInfoVORealTime(Long fancienceId) throws ServiceException;

    /**
     * 通过fancienceId 获取 UserInfoVO list
     * @param fancienceId
     * @return
     * @throws ServiceException
     */
    List<UserInfoVO> listUserInfoVO(List<Long> fancienceId) throws ServiceException;

    /**
     * 通过fancienceId 获取 UserInfoVO list
     * @param fancienceId
     * @return
     * @throws ServiceException
     */
    UserInfoVO getUserInfoVO(Long fancienceId) throws ServiceException;

}
