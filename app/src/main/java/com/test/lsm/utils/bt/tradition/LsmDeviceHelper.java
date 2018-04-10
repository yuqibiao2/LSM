package com.test.lsm.utils.bt.tradition;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * 功能：Lsm蓝牙设备相关
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/4
 */
public class LsmDeviceHelper {

    private static final boolean D = true;
    private static final String TAG = "LsmDeviceHelper";


    // Name for the SDP record when creating server socket
    private static final String NAME = "Lsm";
    // Unique UUID for this application
    // SPP UUID: 00001101-0000-1000-8000-00805F9B34FB
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private int mState;//设备连接状态

    //-----状态
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private final BluetoothAdapter mBTAdapter;
    private final Handler mHandler;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    public LsmDeviceHelper(Context context, Handler handler){
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private BluetoothServerSocket mmServerSocket = null;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = mBTAdapter.listenUsingRfcommWithServiceRecord(NAME, SPP_UUID);
                mmServerSocket = tmp;
            } catch (IOException e) {
                Log.e(TAG, "listen() failed: " + e.getMessage());
            }

        }

        public void run() {
            Log.d(TAG, "BEGIN mAcceptThread" + this);
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (mState != STATE_CONNECTED) {
                try {
                    if (mmServerSocket != null) {
                        socket = mmServerSocket.accept();
                        Log.d(TAG, "accept success");
                    } else {
                        sleep(1);
                        continue;
                    }
                } catch (IOException e) {
                    Log.e(TAG, String.format("accept() failed: %s", e.getMessage()));
                    break;
                } catch (InterruptedException e) {
                    Log.e(TAG, String.format("No bluetooth: %s", e.getMessage()));
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (LsmDeviceHelper.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                manageConnectedSocket(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                    Log.i(TAG, "mmServerSocket is closed");
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "END mAcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket, because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            // Cancel discovery because it will slow down the connection
            mBTAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException e) {
                Log.e(TAG, "connect socket failed", e);
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (LsmDeviceHelper.this) {
                mConnectThread = null;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte mEtx;

        public boolean mResponseUpdated = false;
        public byte[] mResponse = new byte[512];
        public int mResponseLength = 0;

        public ConnectedThread(BluetoothSocket socket) {
            Log.i(TAG, "create ConnectedThread" + this);

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mEtx = 0x0A;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[512];
            int bytes = 0;
            int offset = 0;
            int pktlength = 0;
            boolean isPkt = false;
            stopThread = false;

            // Keep listening to the InputStream while connected
            while (!stopThread) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes == 0) {
                        sleep(1);
                        continue;
                    }



                    offset += mmInStream.read(buffer, offset, 1);
                    for (int i = 0; i < offset; i++) {
                        if (buffer[i] == mEtx) {
                            pktlength = i;
                            isPkt = true;
                            break;
                        }
                    }

                    if (isPkt) {
                        parse(buffer, pktlength);
                        Arrays.fill(buffer, (byte) 0);
                        offset = 0;
                        pktlength = 0;
                        isPkt = false;
                    }

                    sleep(0);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                } catch (InterruptedException ie) {
                    Log.e("dsm362", ie.getMessage());
                    ;
                }
            }
            Log.i(TAG, "Stop mConnectedThread");
        }

        private boolean stopThread = false;

        public void cancel() {
            try {
                stopThread = true;
                mmInStream.close();
                mmOutStream.close();
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param outbuffer The bytes to write
         */
        public void write(byte[] outbuffer) {
            try {
                mmOutStream.write(outbuffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void write(byte singleByte) {
            try {
                mmOutStream.write(singleByte);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        private void parse(byte[] buffer, int length) {
            String str_ack = new String(buffer, 0, length);
            Log.i("dsm362", String.format("rx packet = %s", str_ack));
            parseSwing(buffer, length);
        }

        private void parseSwing(byte[] buffer, int length) {
            try {
                int datalength = length - 3;
                byte cmd = buffer[1];
                byte[] data = new byte[datalength];

                System.arraycopy(buffer, 2, data, 0, datalength);

                switch (cmd) {
                    //--TODO 通过蓝牙收到的信息的处理
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception during parseSwing", e);
            }
        }

        private void sync_update(byte[] buffer, int length) {
            System.arraycopy(buffer, 0, mResponse, 0, length);
            mResponseLength = length;
            mResponseUpdated = true;
        }

        /*
        private byte calcByte(byte origin) {
        	byte ret = 0x00;
        	if(origin < 0x0a) {
        		ret = (byte)(origin + 0x30);
        	}
        	else
        	{
        		ret = (byte)(origin + 0x37);
        	}
        	return ret;
        }

        private String byteArrayToHex(byte[] ba) {
            if (ba == null || ba.length == 0) {
                return null;
            }

            StringBuffer sb = new StringBuffer(ba.length * 2);
            String hexNumber;
            for (int x = 0; x < ba.length; x++) {
                hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

                sb.append(hexNumber.substring(hexNumber.length() - 2));
            }
            return sb.toString();
        } */
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }


    /**
     * --TODO 连接失败
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constant.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TOAST, "Scanner connection fail");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        LsmDeviceHelper.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        mHandler.obtainMessage(Constant.MESSAGE_LOST).sendToTarget();

        //Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        //Bundle bundle = new Bundle();
        //bundle.putString(MainActivity.TOAST, "Device connection was lost");
        //msg.setData(bundle);
        //mHandler.sendMessage(msg);

        LsmDeviceHelper.this.start();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void manageConnectedSocket(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(0);
        Bundle bundle = new Bundle();
        bundle.putString("SCANNER_ADDR", device.getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        try {
            if (D) Log.d("dsm362", "mSwing.start begin");

            // Cancel any thread attempting to make a connection
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }

            // Cancel any thread currently running a connection
            if (mConnectedThread != null) {
                mConnectedThread.cancel();
                mConnectedThread = null;
            }

            // Start the thread to listen on a BluetoothServerSocket
            if (mAcceptThread == null && mBTAdapter != null) {
                mAcceptThread = new AcceptThread();
                mAcceptThread.start();
            }
            setState(STATE_LISTEN);
        } catch (Exception e) {
            if (D) Log.d("dsm362", "mSwing.start: " + e.getMessage());
        }
        if (D) Log.d("dsm362", "mSwing.start end");
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        try {
            Log.d("dsm362", "mSwing.stop begin");

            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
            if (mConnectedThread != null) {
                mConnectedThread.cancel();
                mConnectedThread = null;
            }
            if (mAcceptThread != null) {
                mAcceptThread.cancel();
                mAcceptThread = null;
            }

            setState(STATE_NONE);
        } catch (Exception e) {
            Log.e("dsm362", "mSwing.stop: " + e.getMessage());
        }
        Log.d("dsm362", "mSwing.stop end");
    }

    public synchronized boolean isConnected() {
        if (mConnectedThread != null) return true;
        else return false;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constant.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }


}
