package news.example.zdw.serporttest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * 创 建 人: tangchao
 * 创建日期: 2017/4/21 15:46
 * 修改时间：testdsdf
 * 修改备注：
 */
public class SerPortUtil {
    private  MsgCallback                callback;
    private   TTT         app1;//串口1
    protected OutputStream               mOutputStream1;
    private   InputStream                mInputStream1;
    protected SerialPort                 mSerialPort1;
    private   ReadThread1 mReadThread1;

    //
    private   TTT2        app2;//串口2
    protected OutputStream               mOutputStream2;
    private   InputStream                mInputStream2;
    protected SerialPort                 mSerialPort2;
    private   ReadThread2 mReadThread2;

    String str = "";
    String str2 = "";
    private boolean istrue1;//用来控制串口1读的线程
    private boolean istrue2;//用来控制串口2读的线程




    public void setMsgCallback(MsgCallback back) {
        this.callback = back;
        init();
    }



    private void init() {
        app1 = new TTT();
        if (app1 != null) {
            istrue1=true;
            open1();//打开串口1
        }
       /* app2 = new TTT2();
        if (app2 != null) {
            istrue2=true;
            open2();//打开串口1
        }*/

    }
    //初始串口类1
    private class TTT {

        private SerialPort mSerialPort1 = null;

        public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
            if (mSerialPort1 == null) {

                String path = "/dev/ttyUSB0";  //你要打开的安卓串口名
                int baudrate = 115200;//设置波特率

				/* Open the serial port */
                mSerialPort1 = new SerialPort(new File(path), baudrate, 0);
            }
            return mSerialPort1;
        }

        //关闭串口
        public void closeSerialPort1() {
            if (mSerialPort1 != null) {
                mSerialPort1.close();
                mSerialPort1 = null;
            }
        }
    }

    //打开
    boolean open1() {
        boolean enable = true;
        try {

            mSerialPort1 =app1.getSerialPort();
            if (mSerialPort1!=null) {
                mOutputStream1 = mSerialPort1.getOutputStream();
                mInputStream1 = mSerialPort1.getInputStream();
                LogUtils.showLogI("open");
			/* Create a receiving thread */
                if (mInputStream1 != null) {
                    mReadThread1 = new ReadThread1();
                    mReadThread1.start();
                    sleep(100);
                }
            }

        } catch (SecurityException e) {

            callback.OnMessage("error_security");
        } catch (IOException e) {

            callback.OnMessage("error_unknown");
        } catch (InvalidParameterException e) {

            callback.OnMessage("error_configuration");
        }

        return true;
    }

    private void sleep(int ms) {

        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ReadThread1 extends Thread {
        byte[] buffer = new byte[1024];
        int size;

        @Override
        public void run() {
            super.run();
            while (istrue1) {

                try {

                    size = mInputStream1.read(buffer);
                   /* final byte[] buf = new byte[size];
                     System.arraycopy(buffer, 0, buf, 0, size);*/
                    for (int i = 0; i < size; i++) {
                        str = str + (String.format("%02X ",buffer[i] & 0xff)) + " ";
                    }
                    callback.OnReceive(str);
                    LogUtils.showLogI(str);

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    //初始串口类2
    private class TTT2 {
        //		public SerialPortFinder mSerialPort1Finder = new SerialPortFinder();
        private SerialPort mSerialPort2 = null;

        public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
            if (mSerialPort2 == null) {

                String path = "/dev/ttyUSB2";  //你要打开的安卓串口名
                int baudrate = 115200;//设置波特率

				/* Open the serial port */
                mSerialPort2 = new SerialPort(new File(path), baudrate, 0);
            }
            return mSerialPort2;
        }

        //关闭串口
        public void closeSerialPort2() {
            if (mSerialPort2 != null) {
                mSerialPort2.close();
                mSerialPort2 = null;
            }
        }
    }

    //打开
    boolean open2() {
        boolean enable = true;
        try {
            mSerialPort2 =app2.getSerialPort();
            if (mSerialPort2!=null) {
                mOutputStream2 = mSerialPort2.getOutputStream();
                mInputStream2 = mSerialPort2.getInputStream();
                LogUtils.showLogI("open");
			/* Create a receiving thread */
                if (mInputStream2 != null) {
                    mReadThread2 = new ReadThread2();
                    mReadThread2.start();
                    sleep(100);
                }
            }

        } catch (SecurityException e) {

            callback.OnMessage("error_security");
        } catch (IOException e) {

            callback.OnMessage("error_unknown");
        } catch (InvalidParameterException e) {

            callback.OnMessage("error_configuration");
        }

        return true;
    }



    private class ReadThread2 extends Thread {
        byte[] buffer = new byte[1024];
        int size;


        @Override
        public void run() {
            super.run();
            while (istrue2) {

                try {
                    sleep(1);
                    size = mInputStream2.read(buffer);
                    /*final byte[] buf = new byte[size];
                    System.arraycopy(buffer, 0, buf, 0, size);*/

                    // 数据
                    for (int i = 0; i < size; i++) {
                        str2 = str2 + (String.format("%02X ",buffer[i] & 0xff)) + " ";
                    }
                    LogUtils.showLogI("st2"+str2);

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }



    //发送指令给串口
    public void SendConfigCmd(byte[] b) {
        try {
            //byte cmd[]={(byte)0x20,(byte)0x00,(byte)0x52,(byte)0x00,(byte)0xad,(byte)0x03};
            if (mOutputStream1!=null){
                mOutputStream1.write(b);
            }
        } catch (IOException e) {


        }
    }


    public void stopSerialport(){
        app1.closeSerialPort1();
        istrue1=false;
      /*  app2.closeSerialPort2();
        istrue2=false;*/

    }
}
