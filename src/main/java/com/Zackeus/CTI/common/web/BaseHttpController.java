package com.Zackeus.CTI.common.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.xml.rpc.ServiceException;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;

/**
 * 
 * @Title:BaseHttpController
 * @Description:TODO(接口Controller基类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月28日 下午2:21:12
 */
public abstract class BaseHttpController extends BaseController {
	
	/**
	 * 
	 * @Title：handleMissingServletRequestParameterException
	 * @Description: TODO(400 - 缺少请求参数)
	 * @see：
	 * @param e
	 */
	@ExceptionHandler({MissingServletRequestParameterException.class})
	public String handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response,
			MissingServletRequestParameterException e) {
        return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "缺少请求参数：" + e.getMessage()));
	}
	
	/**
	 * 
	 * @Title：handleHttpMessageNotReadableException
	 * @Description: TODO(400 - 参数解析失败)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({HttpMessageNotReadableException.class})
	public String handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response,
			HttpMessageNotReadableException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数解析失败：" + e.getMessage()));
	}
	
    /**
     * 
     * @Title：runTimeException
     * @Description: TODO(400 - 参数校验异常)
     * @see：
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public String methodArgumentNotValidExceptio(HttpServletRequest request, HttpServletResponse response, 
    		MethodArgumentNotValidException e) {
    	String errorMesssage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    	return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数校验异常：" + errorMesssage));
    }
    
    /**
     * 
     * @Title：methodArgumentNotValidExceptio
     * @Description: TODO(400 - 参数绑定失败)
     * @see：
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({ BindException.class })
    public String handleBindException(HttpServletRequest request, HttpServletResponse response, 
    		BindException e) {
    	String errorMesssage = e.getBindingResult().getFieldError().getField();
    	return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数绑定失败：" + errorMesssage));
    }

    /**
     * 
     * @Title：bindException
     * @Description: TODO(400 - 参数校验异常)
     * @see：
     * @param request
     * @param response
     * @param e
     * @return
     */
	@ExceptionHandler({ConstraintViolationException.class})
    public String handleServiceException(HttpServletRequest request, HttpServletResponse response, 
    		ConstraintViolationException e) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    ConstraintViolation<?> violation = violations.iterator().next();
	    String message = violation.getMessage();
	    return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数校验异常:" + message));
    }
	
	/**
	 * 
	 * @Title：bindException
	 * @Description: TODO(400 - 参数验证失败)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ValidationException.class})
    public String handleValidationException(HttpServletRequest request, HttpServletResponse response, 
    		ValidationException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数验证失败：" + e.getMessage()));
    }
	
	/**
	 * 
	 * @Title：handleIllegalArgumentException
	 * @Description: TODO(400 - 参数不合法)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({IllegalArgumentException.class})
    public String handleIllegalArgumentException(HttpServletRequest request, HttpServletResponse response, 
    		IllegalArgumentException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "参数不合法：" + e.getMessage()));
    }
	
	/**
	 * 
	 * @Title：handleIllegalAccessException
	 * @Description: TODO(400 - 安全权限异常)
	 * @see：安全权限异常，一般来说，是由于java在反射时调用了private方法所导致的
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({IllegalAccessException.class})
    public String handleIllegalAccessException(HttpServletRequest request, HttpServletResponse response, 
    		IllegalAccessException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_BAD_REQUEST, "安全权限异常：" + e.getMessage()));
    }
	
	/**
	 * 
	 * @Title：handleHttpRequestMethodNotSupportedException
	 * @Description: TODO(405 - 不支持当前请求方法)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public String handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, 
    		HttpRequestMethodNotSupportedException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_METHOD_NOT_ALLOWED, "不支持当前请求方法：" + e.getMessage()));
    }
	
	/**
	 * 
	 * @Title：handleHttpMediaTypeNotSupportedException
	 * @Description: TODO(415 - 不支持当前媒体类型)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public String handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpServletResponse response,
			HttpMediaTypeNotSupportedException e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, "不支持当前媒体类型：" + e.getMessage()));
	}
    
	/**
	 * 
	 * @Title：authorizationException
	 * @Description: TODO(403 - 授权登录异常)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
    	return renderString(response, new AjaxResult(HttpStatus.SC_FORBIDDEN, "当前账号无权进行此操作!!!"));
    }
    
    /**
     * 
     * @Title：handleServiceException
     * @Description: TODO(500 - 业务逻辑异常)
     * @see：
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(HttpServletRequest request, HttpServletResponse response, ServiceException e) {
    	return renderString(response, new AjaxResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, "业务逻辑异常：" + e.getMessage()));
    }
    
    /**
     * 
     * @Title：handleException
     * @Description: TODO(数据库异常)
     * @see：
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleException(HttpServletRequest request, HttpServletResponse response, DataIntegrityViolationException e) {
    	return renderString(response, new AjaxResult(HttpStatus.SC_SQL_SERROR, "操作数据库异常：" + e.getMessage()));
    }
    
	/**
	 * 
	 * @Title：schedulerException
	 * @Description: TODO(自定义异常) 
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler({ MyException.class })
	public String schedulerException(HttpServletRequest request, HttpServletResponse response, MyException e) {
		return renderString(response, new AjaxResult(e.getErrorCode(), e.getMessage(), e.getObject()));
	}
	
	/**
	 * 
	 * @Title：exception
	 * @Description: TODO(未知异常)
	 * @see：
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public String exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
		return renderString(response, new AjaxResult(HttpStatus.SC_UNKNOWN, "未知的错误：" + e.getMessage()));
	}

}
