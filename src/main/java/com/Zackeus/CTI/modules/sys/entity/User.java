package com.Zackeus.CTI.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.annotation.SupCol;
import com.Zackeus.CTI.common.annotation.validator.UserValidator;
import com.Zackeus.CTI.common.config.GlobalConfig;
import com.Zackeus.CTI.common.entity.DataEntity;
import com.Zackeus.CTI.common.service.valid.UpdateVaild;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.agent.entity.AgentUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 
 * @Title:User
 * @Description:TODO(用户Entity)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月10日 上午11:08:54
 */
@UserValidator(groups = {UpdateVaild.class})
@Component
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;		// 归属公司
	private Office office;		// 归属部门
	private String loginName;	// 登录名
	private String password;	// 密码
	private String no;			// 工号
	private String name;		// 姓名
	private String email;		// 邮箱
	private String phone;		// 电话
	private String mobile;		// 手机
	private String userType;	// 用户类型(键值)
	private String userTypeName;// 用户类型(字符)
	private String loginIp;		// 最后登陆IP
	private Date loginDate;		// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;		// 头像

	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码
	
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	
	private AgentUser agentUser;	// 坐席用户
	private Role role;				// 根据角色查询用户条件
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	public User() {
		super();
		this.loginFlag = GlobalConfig.YES;
	}
	
	public User(String id){
		super(id);
	}

	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}
	
	public User(String id, AgentUser agentUser){
		super(id);
		this.agentUser = agentUser;
	}
	
	public User(Principal principal){
		super(principal.getId());
		this.name = principal.getName();
		this.loginName = principal.getLoginName();
		this.agentUser = principal.getAgentUser();
	}

	public User(Role role){
		super();
		this.role = role;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	@SupCol(isUnique="true", isHide="true")
	public String getId() {
		return id;
	}
	
	@NotNull(message="归属公司不能为空")
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@NotNull(message="归属部门不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 
	 * @Title：getPassword
	 * @Description: TODO()
	 * @see：
	 * 注意使用 @JsonIgnore
	 * 在json序列化时，将java bean中的一些属性忽略掉
	 * @return
	 */
	@JsonIgnore
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}
	
	@Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	public AgentUser getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(AgentUser agentUser) {
		this.agentUser = agentUser;
	}

	public boolean isAdmin() {
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id) {
		return StringUtils.isNotBlank(id) && StringUtils.equals(ADMIN_ID, id);
	}
	
	public static final String ADMIN_ID = "1";
}