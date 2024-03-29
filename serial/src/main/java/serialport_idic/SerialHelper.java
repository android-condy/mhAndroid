package serialport_idic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;


public abstract class SerialHelper {

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    //    private SendThread mSendThread;
    private String sPort = "/dev/ttyS2";
    private int iBaudRate = 9600;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;

    private final long delayTime = 50;

    //----------------------------------------------------

    /**
     * @param sPort     串口地址
     * @param iBaudRate 波特率
     */
    public SerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }


    public SerialHelper() {
        this("/dev/ttyS3", "9600");
    }

    /**
     * 默认波特率为9600
     *
     * @param sPort 串口地址
     */
    public SerialHelper(String sPort) {
        this(sPort, 9600);
    }

    /**
     * @param sPort     串口地址
     * @param sBaudRate 波特率
     */
    public SerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }

    //----------------------------------------------------

    /**
     * 打开串口
     *
     * @throws IOException
     * @throws InvalidParameterException
     */
    public void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
//        mSendThread = new SendThread();
//        mSendThread.setSuspendFlag();
//        mSendThread.start();
        _isOpen = true;
    }

    //----------------------------------------------------
    public void close() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
//        mSendThread = null;
        mReadThread = null;
        _isOpen = false;
    }

    //----------------------------------------------------

    /**
     * 发送数据
     *
     * @param bOutArray 需要发送的二进制数据
     */
    public void send(byte[] bOutArray) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(bOutArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //----------------------------------------------------
    private class ReadThread extends Thread {

        private byte[] buffer = new byte[1024];

        private byte[] cacheBuffer = new byte[4028];

        private int readLength = 0;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) return;
                    readLength = 0;
                    while (mInputStream.available() > 0) {
                        int size = mInputStream.read(buffer);
                        System.arraycopy(buffer, 0, cacheBuffer, readLength, size);
                        readLength += size;
                        try {
                            Thread.sleep(50);//��ʱ50ms
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (readLength > 0) {
                        ComBean ComRecData = new ComBean(sPort, cacheBuffer, readLength);
                        String string = Arrays.toString(MyFunc.toHexString(ComRecData.bRec).split(" "));
                        onDataReceived(ComRecData);
                    }
                    try {
                        Thread.sleep(delayTime);//��ʱ50ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public boolean isOpen() {
        return _isOpen;
    }
    //----------------------------------------------------

    /**
     * 串口接收到数据的回调函数
     *
     * @param ComRecData 返回数据，包含串口地址，接收时间，接收到的珊瑚橘
     */
    protected abstract void onDataReceived(ComBean ComRecData);
}