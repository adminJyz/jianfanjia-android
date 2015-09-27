package com.jianfanjia.cn.dao;

import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.db.DBHelper;

public class NotifyMessageDao implements BaseDao<NotifyMessage> {
	private Context context;
	private DBHelper helper;
	private Dao<NotifyMessage, Integer> dao;

	public NotifyMessageDao(Context context) {
		this.context = context;
		try {
			helper = DBHelper.getHelper(context);
			dao = helper.getDao(NotifyMessage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(NotifyMessage message) {
		try {
			dao.create(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(List<NotifyMessage> list) {
		try {
			for (NotifyMessage message : list) {
				dao.create(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(NotifyMessage message) {
		try {
			dao.delete(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(NotifyMessage message) {
		try {
			dao.update(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<NotifyMessage> query() {
		List<NotifyMessage> list = null;
		try {
			list = dao.queryForAll();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过消息类型查
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<NotifyMessage> getNotifyListByType(String type) {
		List<NotifyMessage> list = null;
		try {
			list = dao.queryBuilder().where().eq("type", type).query();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NotifyMessage findOneById(int id) {
		NotifyMessage notifyMessage = null;
		try {
			notifyMessage = dao.queryForId(id);
			return notifyMessage;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
