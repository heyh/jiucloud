package sy.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InterfaceDes  {

	/**
	 * Uri的值
	 * @return
	 */
	String value();
	/**
	 * 该Uri对应的服务名
	 * @return
	 */
	String name();
	
	
}
