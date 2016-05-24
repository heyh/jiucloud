package sy.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.TicketDaoI;
import sy.model.po.TTicket;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.Ticket;
import sy.service.TicketServiceI;
import sy.service.UserServiceI;
import sy.util.DateKit;

import java.util.*;

/**
 * Created by heyh on 16/5/17.
 */

@Service("ticketService")
public class TicketServiceImpl implements TicketServiceI {

    @Autowired
    private TicketDaoI ticketDao;

    @Autowired
    private UserServiceI userService;


    @Override
    public DataGrid dataGrid(Ticket ticket, PageHelper ph, List<Integer> ugroup, String ticketType, String keyword) {
        DataGrid dg = new DataGrid();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from TTicket t  where isDelete=0";
        hql += whereHql(ticket, params, ugroup, keyword);

        List<TTicket> tTicketList = ticketDao.find(hql, params, ph.getPage(), ph.getRows());
        dg.setTotal(ticketDao.count("select count(*) " + hql, params));

        List<Ticket> ticketList = new ArrayList<Ticket>();
        for (TTicket tTicket : tTicketList) {
            Ticket t = new Ticket();
            t.setId(tTicket.getId());
            t.setTicketType(tTicket.getTicketType());
            t.setTicketName(tTicket.getTicketName());
            t.setTicketDate(tTicket.getTicketDate());
            t.setSupplier(tTicket.getSupplier());
            t.setConsumer(tTicket.getConsumer());
            t.setTaxNo(tTicket.getTaxNo());
            t.setAddress(tTicket.getAddress());
            t.setTaxBank(tTicket.getTaxBank());
            t.setTaxAccount(tTicket.getTaxAccount());
            t.setTaxStatus(tTicket.getTaxStatus());
            t.setUnit(tTicket.getUnit());
            t.setCount(tTicket.getCount());
            t.setPrice(tTicket.getPrice());
            t.setSpecifications(tTicket.getSpecifications());
            t.setMoney(tTicket.getMoney());
            t.setLinkPerson(tTicket.getLinkPerson());
            t.setLinkPhone(tTicket.getLinkPhone());
            t.setCreateTime(tTicket.getCreateTime());
            t.setUpdateTime(tTicket.getUpdateTime());
            t.setUid(tTicket.getUid());
            t.setUname(tTicket.getUname());
            t.setCid(tTicket.getCid());
            t.setCompany(tTicket.getCompany());
            t.setContract(tTicket.getContract());
            t.setTicketStatus(tTicket.getTicketStatus());
            t.setAuthStatus(tTicket.getAuthStatus());
            t.setReciveStatus(tTicket.getReciveStatus());
            ticketList.add(t);
        }
        dg.setRows(ticketList);

        return dg;
    }

    private String whereHql(Ticket cmodel, Map<String, Object> params, List<Integer> ugroup, String keyword) {
        String hql = " ";
        if (cmodel != null) {

            if (cmodel.getSupplier() != null && cmodel.getSupplier().length() > 0) {
                hql += " and supplier like :supplier ";
                params.put("supplier", "%%" + cmodel.getSupplier() + "%%");
            }
            if (cmodel.getConsumer() != null && cmodel.getConsumer().length() > 0) {
                hql += " and consumer like :consumer ";
                params.put("consumer", "%%" + cmodel.getConsumer() + "%%");
            }
            if (cmodel.getStartTime() != null && cmodel.getStartTime().length() > 0) {
                hql += " and t.createTime >= :startTime";
                params.put("startTime", DateKit.strToDateOrTime(cmodel.getStartTime()));
            }
            if (cmodel.getEndTime() != null && cmodel.getEndTime().length() > 0) {
                hql += " and t.createTime <= :endTime";
                params.put("endTime", DateKit.strToDateOrTime(cmodel.getEndTime()));
            }
        }

        if (keyword != null && !keyword.equals("")) {
            hql += " and ( supplier like :supplier ";
            params.put("supplier", "%%" + keyword + "%%");

            hql += " or consumer like :consumer ";
            params.put("consumer", "%%" + keyword + "%%");

            hql += " or ticketName like :ticketName ";
            params.put("ticketName", "%%" + keyword + "%%");

            hql += " or taxNo like :taxNo ";
            params.put("taxNo", "%%" + keyword + "%%");

            hql += " or address like :address ";
            params.put("address", "%%" + keyword + "%%");

            hql += " or taxBank like :taxBank ";
            params.put("taxBank", "%%" + keyword + "%%");

            hql += " or taxAccount like :taxAccount ";
            params.put("taxAccount", "%%" + keyword + "%%");

            hql += " or uname like :uname ";
            params.put("uname", "%%" + keyword + "%%");

            hql += " or ticketStatus like :ticketStatus ";
            params.put("ticketStatus", "%%" + keyword + "%%");

            hql += " or authStatus like :authStatus ";
            params.put("authStatus", "%%" + keyword + "%%");

            hql += " or reciveStatus like :reciveStatus ";
            params.put("reciveStatus", "%%" + keyword + "%%");

            hql += " or contract like :contract )";
            params.put("contract", "%%" + keyword + "%%");
        }
        String uids = StringUtils.join(ugroup, ",");
        hql += " and uid in (" + uids + ")";

        hql += " and ticketType = :ticketType ";
        params.put("ticketType", cmodel.getTicketType());

        hql += " order by t.id desc";

        return hql;
    }

    @Override
    public TTicket add(TTicket tTicket) {
        tTicket.setCreateTime(new Date());
        ticketDao.save(tTicket);
        return tTicket;
    }

    @Override
    public void delete(String id) {
        try {
            if (id != null) {
                TTicket info = detail(id);
                if (info != null) {
                    info.setIsDelete(1);
                    update(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(TTicket info) {
        TTicket ticket = ticketDao.get(TTicket.class, info.getId());
        BeanUtils.copyProperties(info, ticket);
    }

    @Override
    public TTicket detail(String id) {
        if (id == null) {
            return null;
        }
        TTicket tTicket = ticketDao.get(" FROM TTicket t  where 1=1 and isDelete=0 and id= '" + id + "'");
        return tTicket;
    }

    @Override
    public void del(TTicket ticket) {
        TTicket tTicket = ticketDao.get(TTicket.class, ticket.getId());
        BeanUtils.copyProperties(ticket, tTicket);
    }
}
