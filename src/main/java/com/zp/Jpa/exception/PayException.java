package com.zp.Jpa.exception;

public class PayException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//无参构造方法
	public PayException(){
		super("支付异常");
	}
	//有参的构造方法
	public PayException(String info){
		super(info);
	}
	
	// 用指定的详细信息和原因构造一个新的异常
    public PayException(String info, Throwable cause){

        super(info,cause);
    }

    //用指定原因构造一个新的异常
     public PayException(Throwable cause) {

         super(cause);
     }
     public String  test() {

         return "我是test";
     }
}
