package sy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.MessageDaoI;
import sy.dao.MessageTextDaoI;
import sy.dao.UserDaoI;
import sy.model.Tuser;
import sy.model.po.MessageText;
import sy.model.po.TInform;
import sy.pageModel.AppSearch;
import sy.pageModel.DataGrid;
import sy.pageModel.Message;
import sy.pageModel.PageHelper;
import sy.service.MessageServiceI;

/**
 * **************************************************************** 文件名称 :
 * IMUserServiceImpl.java 作 者 : Administrator 创建时间 : 2014年12月22日 下午3:08:11 文件描述
 * : 轻应用Service 版权声明 : 修改历史 : 2014年12月22日 1.00 初始版本
 *****************************************************************
 */
@Service
public class MessageServiceImpl implements MessageServiceI {

	@Autowired
	private MessageTextDaoI messageTextDao;

	@Autowired
	private MessageDaoI messageDao;

	@Autowired
	private UserDaoI userDao;

	@Override
	public DataGrid dataGrid(AppSearch app, PageHelper ph, String fwName) {
		return null;
	}

	/**
	 * 发件删除信息
	 */
	@Override
	public void delete(String id) {
		try {
			if (id != null) {
				sy.model.po.Message m = this.messageDao
						.get("from Message m where m.id=" + id);
				// 更新已读
				m.setSenderIsdeldate(new Date());
				m.setSenderIsdel(0);// 删除
				this.messageDao.update(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 收件删除信息
	 */
	@Override
	public void deleteRec(String id) {
		try {
			if (id != null) {
				sy.model.po.Message m = this.messageDao
						.get("from Message m where m.id=" + id);
				// 更新已读
				m.setIsdeldate(new Date());
				m.setIsdel(0);// 删除
				this.messageDao.update(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void huifu(String id, String uid) {
		try {
			if (id != null) {
				sy.model.po.Message m = this.messageDao
						.get("from Message m where m.id=" + id);
				if (m.getSendId().equals(uid)) {
					m.setSenderIsdel(1);
				} else {
					m.setIsdel(1);
				}
				this.messageDao.update(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cddelete(String id, String uid) {
		try {
			if (id != null) {
				sy.model.po.Message m = this.messageDao
						.get("from Message m where m.id=" + id);
				if (m.getSendId().equals(uid)) {
					m.setSenderIsdel(2);
				} else {
					m.setIsdel(2);
				}
				this.messageDao.update(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void savemsg(Message msg, String uid) {
		String[] s = msg.getSendName().split(",");
		for (String uids : s) {
			Map<String, Object> m = new HashMap<String, Object>();
			// m.put("id", "16047");
			m.put("id", uids);
			Tuser u = this.userDao.get("from Tuser t where t.id=:id", m);
			System.out.println(u);
			MessageText messageText = new MessageText();
			messageText.setMessage(msg.getMessage());
			messageText.setPdate(new Date());
			messageText.setTitle(msg.getTitle());
			this.messageTextDao.save(messageText);
			if (u != null) {
				sy.model.po.Message ms = new sy.model.po.Message();
				ms.setIsdel(1);
				ms.setMessageid(messageText.getId());
				ms.setSendId(uid);
				ms.setRecid(u.getId());
				ms.setStatue(0);
				ms.setSenderIsdel(1);
				this.messageDao.save(ms);
			}
		}

	}

	@Override
	public List<TInform> getAllInfo(String fwName, String infoTitle) {
		// TODO Auto-generated method stub
		return null;
	}

}
