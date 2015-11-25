package sy.util;




/**
 * PropertyUtil
 * @author ldw
 *
 */
public class PropertyUtil {
	
	
	/**
	 * PropertyUtil
	 */
	private PropertyUtil(){
		
	}
	 

	/**
	 * 获取数据库密码
	 * @return String
	 */
	public static String getUserP() {
		String userP = getName("userp");
		return userP;
	}

	public static String getName(String key){
		return java.util.ResourceBundle.getBundle("config").getString(key);
	}
	/**
	 * 获取用户名
	 * @return String
	 */
	public static String getUserName() {
		String userName = getName("userName");
		return userName;
	}

	/**
	 * 获取数据库URL
	 * @return String
	 */
	public static String getDbUrl() {
		String dburl = getName("dburl");
		return dburl;
	}

	public static String getOpenOffice() {
		String officeHome = getName("officeHome");
		return officeHome;
	}


	/**
	 * 获取数据库SCHEMA
	 * @return String
	 */
	public static String getSchema() {

		String schema = "";
		return schema;
	}

 
	
	/**
	 * getDriverClass
	 * @return String
	 */
	public static String getDriverClass(){
		String driverClass = ""; 
		driverClass = getName("driverClass");
		return driverClass;
	}

    /**
     *
     * @return
     */
    public static String getFilePathHome() {
        String filePathHome = "";
        filePathHome = getName("filepathHome");
        return filePathHome;
    }

    public static String getFileRealPath() {
        String fileRealPath = "";
        fileRealPath = getName("fileRealPath");
        return fileRealPath;
    }

}
