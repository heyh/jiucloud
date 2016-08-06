package sy.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

/**
 * Created by Stomic on 15/6/3.
 */
public class WebResult extends JSONObject {
    public final static int ERROR = 1;
    public final static int SUCCESS = 0;
    private final static String RSP_CODE = "rspCode";
    private final static String RSP_MESSAGE = "data";

    public WebResult ok(){
        this.put(RSP_CODE, SUCCESS);
        return this;
    }

    public WebResult fail(){
        this.put(RSP_CODE,ERROR);
        return this;
    }

    public WebResult set(String key,Object value){
        if (StringUtils.isEmpty(key) || null == value) return this;
        this.put(key,value);
        return this;
    }

    public WebResult setMessage(Object value){
        if (null == value) return this;
        this.put(RSP_MESSAGE,value);
        return this;
    }
}
