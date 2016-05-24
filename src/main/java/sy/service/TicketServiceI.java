package sy.service;

import sy.model.po.TTicket;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.Ticket;

import java.util.List;

/**
 * Created by heyh on 16/5/17.
 */
public interface TicketServiceI {

    /**
     * 列表
     *
     * @param ticket
     * @param ph
     * @return
     */
    public DataGrid dataGrid(Ticket ticket, PageHelper ph,  List<Integer> ugroup, String ticketType, String keyword);

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id);

    /**
     * 添加
     *
     * @param ticket
     * @return
     */
    public TTicket add(TTicket ticket);

    /**
     * 更新
     *
     * @param ticket
     */
    public void update(TTicket ticket);

    TTicket detail(String id);

    void del(TTicket ticket);
}
