package cc.carce.sale.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result 统一响应解构
 */

@Data // See Also:Getter, Setter, RequiredArgsConstructor, ToString,
		// EqualsAndHashCode, Value
@AllArgsConstructor
@NoArgsConstructor
@Builder // 支持构建者模式
@ApiModel(value = "R对象", description = "统一响应对象")
public class R<T> implements Serializable {
	@ApiModelProperty(value = "状态码 0失败 1成功", required = true)
	protected Integer code;
	@ApiModelProperty(value = "提示信息")
	protected String msg;
	@ApiModelProperty(value = "数据")
	protected T data;
	@ApiModelProperty(value = "服务器响应时间 yyyy-MM-dd HH:mm:ss GMT+8", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	protected LocalDateTime responseTime = LocalDateTime.now();

	/**
	 * 响应状态码
	 */
	interface Code {
		int SUCCESS_CODE = 1;
		int FAIL_CODE = 0;
	}

	/**
	 * 响应状态码
	 */
	interface Message {
		String SUCCESS_MESSAGE = "成功";
		String FAIL_MESSAGE = "失败";
	}

	// 构造函数
	private R(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

//----------- 封装常用方法-----------------

	public static <T> R<T> result(Integer code, String message, T data) {
		return new R<T>(code, message, data);
	}

	/**
	 * 封装特有的boolean类型 根据boolean类型自动封装返回类型
	 *
	 * @param result 结果
	 * @return R
	 */
	public static R<Boolean> result(Boolean result) {
		if (result) {
			return ok();
		} else {
			return fail();
		}
	}

	public static <T> R<T> ok() {
		return result(Code.SUCCESS_CODE, Message.SUCCESS_MESSAGE, null);
	}

	public static <T> R<T> ok(T data) {
		return result(Code.SUCCESS_CODE, Message.SUCCESS_MESSAGE, data);
	}

	public static <T> R<T> ok(String message, T data) {
		return result(Code.SUCCESS_CODE, message, data);
	}

	public static <T> R<T> fail() {
		return result(Code.FAIL_CODE, Message.FAIL_MESSAGE, null);
	}

	public static <T> R<T> fail(T data) {
		return result(Code.FAIL_CODE, Message.FAIL_MESSAGE, data);
	}

	public static <T> R<T> fail(String message, T data) {
		return result(Code.FAIL_CODE, message, data);
	}
//
//    //支持各种分页框架
//    public static <T> R<PageResult<T>> ok(IPage<T> page) {
//        return result(Code.SUCCESS_CODE, Message.SUCCESS_MESSAGE, PageResult.page(page));
//    }

	public static <T> R<T> okMsg(String msg) {
		return R.ok(msg, null);
	}

	public static <T> R<T> failMsg(String msg) {
		return R.fail(msg, null);
	}

	public boolean isSuccess() {
		return code != null && Integer.valueOf(1).equals(code);
	}
}
