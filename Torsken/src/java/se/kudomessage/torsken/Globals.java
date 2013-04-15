/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import javax.faces.bean.SessionScoped;

@SessionScoped
public class Globals {
        private static String _accessToken;
	private static String _email;

	public static String getAccessToken() {
		return _accessToken;
	}

	public static void setAccessToken(String accessToken) {
		_accessToken = accessToken;
	}

	public static String getEmail() {
		return _email;
	}

	public static void setEmail(String email) {
		_email = email;
	}
}
