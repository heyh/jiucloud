package sy.service;

import sy.model.po.Discuss;

import java.util.List;

/**
 * Created by heyh on 2017/4/6.
 */
public interface DiscussServiceI {
    public List<Discuss> getDiscussList(String discussId);
    public Discuss addDiscuss(Discuss discuss);
    public void delDiscuss(String id);
    public Discuss detail(String id);
    public void update(Discuss info);

}
