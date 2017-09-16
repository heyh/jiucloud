package sy.service;

import sy.model.Clockingin;

import java.util.List;

/**
 * Created by heyh on 2017/9/16.
 */
public interface ClockinginServiceI {

    public Clockingin Clockingin(Clockingin clockingin);

    public List<Clockingin> getClockingins(List<Integer> ugroup);

    public int approveClockingin(String approveState);
}
