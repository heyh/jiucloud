package sy.dao.impl;

import org.springframework.stereotype.Repository;
import sy.dao.TemplateDaoI;
import sy.model.po.Template;

/**
 * Created by heyh on 16/7/17.
 */
@Repository("templateDao")
public class TemplateDaoImpl extends BaseDaoImpl<Template> implements TemplateDaoI {
}
