package sy.util;

public class ServerInfo {

	// 服务器的真实路径
		private static String _serverPath;

		public static long masBootTime ;
		
		public static String getServerPath() {
			return _serverPath;
		}

		public static void setServerPath(String serverPath) {
			_serverPath = serverPath;
		}

		public static long getMasBootTime() {
			return masBootTime;
		}

		public static void setMasBootTime(long masBootTime) {
			ServerInfo.masBootTime = masBootTime;
		}
}
