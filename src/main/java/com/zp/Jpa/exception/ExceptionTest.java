package com.zp.Jpa.exception;

public class ExceptionTest {
	public void test2() {

        try{

            test1();

        }catch (PayException e){

           RuntimeException exception  =  new RuntimeException(e);
           //exception.initCause(cause)
           throw  exception;
        }

    }

    public void test1() throws PayException{

        throw new PayException("我没钱了");
    }
// main方法
    public static void main(String[] args) throws PayException {

    	ExceptionTest object =  new  ExceptionTest();

        try{

            object.test1();

        }catch(PayException e){

        e.printStackTrace();

        }

    }
}
