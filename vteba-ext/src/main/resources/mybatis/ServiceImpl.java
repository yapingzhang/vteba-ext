package com.vteba.user.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.user.dao.UserDao;
import com.vteba.user.model.User;
import com.vteba.user.model.UserBean;
import com.vteba.user.service.UserService;

/**
 * 用户service业务实现。
 * @author yinlei
 * 2014-2-25 上午11:26:29
 */
@Named
public class UserServiceImpl implements UserService {
	
	@Inject
	private UserDao userDao;

	@Override
	public int count(UserBean userBean) {
		return userDao.count(userBean);
	}

	@Override
	public int deleteBatch(UserBean userBean) {
		return userDao.deleteBatch(userBean);
	}

	@Override
	public int deleteById(Long id) {
		return userDao.deleteById(id);
	}

	@Override
	public int save(User record) {
		return userDao.save(record);
	}

	@Override
	public List<User> queryForList(UserBean userBean) {
		return userDao.queryForList(userBean);
	}

	@Override
	public User get(Long id) {
		return userDao.get(id);
	}

	@Override
	public int updateBatch(User record, UserBean userBean) {
		return userDao.updateBatch(record, userBean);
	}

	@Override
	public int updateById(User record) {
		return userDao.updateById(record);
	}

    @Override
    public int countBy(User params) {
        return userDao.countBy(params);
    }

    @Override
    public int deleteBulks(User params) {
        return userDao.deleteBulks(params);
    }

    @Override
    public List<User> queryList(User params) {
        return userDao.queryList(params);
    }

    @Override
    public List<User> pagedForList(UserBean params) {
        return userDao.pagedForList(params);
    }

    @Override
    public List<User> pagedList(User params) {
        return userDao.pagedList(params);
    }

    @Override
    public int updateBulks(User record, User params) {
        return userDao.updateBulks(record, params);
    }

}
