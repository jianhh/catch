package com.work.admin.schema;

import java.io.Serializable;

public class User  implements Serializable{
	
	private String u_id;
	private String u_name;					//’À∫≈
	private String u_password;			//√‹¬Î
	private String u_create_time;		
	private String u_limit;					//»®œﬁ
	
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_password() {
		return u_password;
	}
	public void setU_password(String u_password) {
		this.u_password = u_password;
	}
	public String getU_create_time() {
		return u_create_time;
	}
	public void setU_create_time(String u_create_time) {
		this.u_create_time = u_create_time;
	}
	public String getU_limit() {
		return u_limit;
	}
	public void setU_limit(String u_limit) {
		this.u_limit = u_limit;
	}
	
}
