package com.everwing.server.admin.util;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.AccountApi;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MyRealm extends AuthorizingRealm {
	@Autowired
	private AccountApi accountApi;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 验证当前登录的Subject
	 * 经测试：本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()的时候
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		// 查询用户
		RemoteModelResult<Account> result = null;
		
		if (Boolean.parseBoolean(System.getProperty("dev", "false"))) {// 开发模式使用写死的账号
			result = new RemoteModelResult<Account>();
			Account account = new Account();
			account.setAccountId("991");
			account.setAccountName("admin");
			account.setPassword("123456");
			result.setModel(account);
		} else {
			result = accountApi.queryByAccountNameAndPsw(token.getUsername(),
					Dict.ACCOUNT_TYPE_ADMIN.getIntValue(), new String(token.getPassword()));
		}
		
		
		if (!result.isSuccess()) {
			throw new ECPBusinessException(ReturnCode.getReturnCodeByValue(result.getCode()));
		}

		Account user = result.getModel();
		AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(new ShiroUser(user),
				user.getPassword(), this.getName());
		return authcInfo;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -1373760761780840081L;
		private Account account;

		public ShiroUser(Account account) {
			super();
			this.account = account;
		}

		public String getUserName() {
			return account.getAccountName();
		}

		public String getUserId() {
			return account.getAccountId();
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return account.getAccountName();
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			if (account.getAccountName() != null) {
				return account.getAccountName().hashCode();
			} else {
				return 0;
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Account) {
				return account.getAccountName().equals(((Account) obj).getAccountName());
			} else {
				return false;
			}
		}

		public Account getUser() {
			return account;
		}
	}
}
