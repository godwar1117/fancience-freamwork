package com.fancience.service.user.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.fancience.api.exception.ServiceException;
import com.fancience.api.protocol.WebFormResult;
import com.fancience.service.user.IUserPubService;
import com.fancience.uc.entity.vo.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Leonid on 18/4/24.
 */
@Service
public class UserInfoPubServiceImpl implements IUserPubService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoPubServiceImpl.class);

    @Value("${uc.query.user.url}")
    private String UC_QUERY_USER_URL;
    @Value("${uc.cache.user.time}")
    private Integer UC_USER_CACHE_TIME = 60 * 60 * 2;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private static final String UC_QUERY_USER_KEY = "fancience:uc:user:";

    @Override
    public List<UserInfoVO> listUserInfoVORealTime(List<Long> fancienceId) throws ServiceException {
        if (fancienceId == null || fancienceId.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        List<String> ids = fancienceId.stream().map(String::valueOf).collect(Collectors.toList());
        params.put("fancienceIds", ids);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        String str = restTemplate.postForObject(UC_QUERY_USER_URL, requestEntity, String.class);
        WebFormResult<List<UserInfoVO>> result = JSON.toJavaObject(JSON.parseObject(str), WebFormResult.class);
        if (result.isSuccess()) {
            return result.getResult();
        }
        throw new ServiceException(result.getMessage());
    }

    @Override
    public UserInfoVO getUserInfoVORealTime(Long fancienceId) throws ServiceException {
        List<UserInfoVO> list = this.listUserInfoVO(Arrays.asList(fancienceId));
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<UserInfoVO> listUserInfoVO(List<Long> fancienceId) throws ServiceException {
        if (fancienceId == null || fancienceId.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // 排序
        Collections.sort(fancienceId);
        String fancience_cache_key = ArrayUtil.join(fancienceId.toArray(),",");
        String md5key = SecureUtil.md5(fancience_cache_key);
        String key = (UC_QUERY_USER_KEY + md5key);
        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNull(obj)) {
            obj = listUserInfoVORealTime(fancienceId);
            redisTemplate.opsForValue().set(key, obj, UC_USER_CACHE_TIME, TimeUnit.SECONDS);
        }
        return (List<UserInfoVO>) obj;
    }

    @Override
    public UserInfoVO getUserInfoVO(Long fancienceId) throws ServiceException {
        if (ObjectUtil.isNull(fancienceId)) {
            throw new IllegalArgumentException();
        }
        String key = (UC_QUERY_USER_KEY + fancienceId);
        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNull(obj)) {
            obj = getUserInfoVORealTime(fancienceId);
            redisTemplate.opsForValue().set(key, obj, UC_USER_CACHE_TIME, TimeUnit.SECONDS);
        }
        return (UserInfoVO) obj;
    }


}
