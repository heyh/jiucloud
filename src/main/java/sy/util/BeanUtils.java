package sy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeanUtils {
    private final static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 根据Map的Key值,转化成对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map map,Class<T> clazz){
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        if (null == map || map.size() == 0)
            return null;
        T object = null;
        try {
            object = clazz.newInstance();
            for (Object key : map.keySet()) {
                Field field = ReflectionUtils.findField(clazz, covertSlashKey(key.toString()));
                if (field != null && !StringUtils.isEmpty(map.get(key.toString()))) {
                    ReflectionUtils.makeAccessible(field);
                    if (!field.getType().isAssignableFrom(Date.class)){
                        field.set(object, typeConverter.convertIfNecessary(map.get(key.toString()).toString(), field.getType()));
                    }else{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(map.get(key.toString()).toString());
                        field.set(object,date);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 根据mapList转成对应的List<T>
     * @param mapList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(List<Map<String,Object>> mapList,Class<T> clazz){
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        if (null == mapList || mapList.size() == 0)
            return null;
        List<T> result = new ArrayList<T>();

        try {
            for (Map obj : mapList) {
                result.add(toBean(obj,clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对象解析出错:"+e.getMessage());
        }
        return result;
    }

    private static String covertSlashKey(String key){
        String indexKey = "_";
        int index = key.indexOf(indexKey);
        if(index > 0){
            key = key.substring(0,index)+String.valueOf(key.charAt(index+1)).toUpperCase()+key.substring(index+2);
            if (key.indexOf(indexKey)>0){
                return covertSlashKey(key);
            }else{
                return key;
            }
        }else{
            return key;
        }
    }
}
