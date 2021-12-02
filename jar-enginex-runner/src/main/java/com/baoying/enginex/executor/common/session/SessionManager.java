package com.baoying.enginex.executor.common.session;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * session管理类
 */
public class SessionManager {
	private static TransmittableThreadLocal<SessionData> session = new TransmittableThreadLocal<SessionData>() {

	};

	public static SessionData getSession() {
		return session.get();
	}

	public static void setSession(SessionData conn) {
		session.set(conn);
	}
}