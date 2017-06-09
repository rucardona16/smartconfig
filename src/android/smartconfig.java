package co.iothings.plugin;

import co.iothings.plugin.BytesIO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import com.ogemray.smartcofig_tcp.model.EGetDevice;
import com.ogemray.smartcofig_tcp.task.TCPSetupTask;
import com.ogemray.smartconfig4.EsptouchTask;
import com.ogemray.smartconfig4.IEsptouchListener;
import com.ogemray.smartconfig4.IEsptouchResult;
import com.ogemray.smartconfig4.IEsptouchTask;
import com.ogemray.smartconfig4.task.__IEsptouchTask;
import com.ogemray.smartconfig4.util.BytesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class smartConfig extends CordovaPlugin {

    public static final String TAG = "DEBUG";
    public EGetDevice eGetDevice = new EGetDevice();

    /**the async task used to configuration*/
    EsptouchAsyncTask3 esptouchAsyncTask3;
    
    private Handler handler;
    CallbackContext receivingCallbackContext = null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        receivingCallbackContext = callbackContext;    
        if (action.equals("start")) {
            String ssid = args.getString(0);
            String password = args.getString(1);

            eGetDevice.setUserMarking(args.getString(2));
            eGetDevice.setOrderMarking(args.getString(3));
            eGetDevice.setDeviceName(args.getString(4));

            this.start(ssid, password);
            return true;
        } else if (action.equals("cancel")){
            esptouchAsyncTask3.stopEsptouchTask();
            PluginResult resultCancel = new PluginResult(PluginResult.Status.OK, "cancel samrtConfig");
            resultCancel.setKeepCallback(true);           // keep callback after this call
            receivingCallbackContext.sendPluginResult(resultCancel);
        }
        return false;
    }

    private void start(String apSsid, String apPassword) {
        String wifiName = apSsid;
        String  wifiPassword = apPassword;

        //start execute
        esptouchAsyncTask3 = new EsptouchAsyncTask3();
        esptouchAsyncTask3.execute(wifiName, wifiPassword, null, (byte) 0x09);
    }

    /**
     *  after receiver multicast datagram from device ,the method will be invoke
     * @param result the result from smart configuration
     */
    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        cordova.getThreadPool().execute(new Runnable() {

            @Override
            public void run() {
               /* String text = "Found new device ,data length="+result.getUserData().length; //result.getBssid()
                Toast.makeText(MainActivity.this, text,
                        Toast.LENGTH_LONG).show();*/
            }

        });
    }

    /**
     * callback
     */
    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    /**
     * 
     * 
     * the task of let the device connect to router 
     *
     */
    private class EsptouchAsyncTask3 extends AsyncTask<Object, Void, List<IEsptouchResult>> {

        //private ProgressDialog mProgressDialog;

        /*the task of the configuration */
        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();
        
        //after receive udp datagram from device,this task is to connect to device with tcp connect
        //and send bind data to device,you can custom fill some params in device
        private TCPAsyncTask3 tcpAsyncTask3;

        /**
         * stop configurate
         */
        public void stopEsptouchTask() {
            synchronized (mLock) {
                if (__IEsptouchTask.DEBUG) {
                    Log.i(TAG, "config is canceled");
                }
                if (mEsptouchTask != null) {
                    mEsptouchTask.interrupt();
                }
                /*if(mProgressDialog!=null){
                	mProgressDialog.dismiss();
                }*/
            }
        }


        @Override
        protected void onPreExecute() {

            /*mProgressDialog = new ProgressDialog(cordova.getActivity());
            mProgressDialog
                    .setMessage("EG003 is configuring, please wait for a moment...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog is canceled");
                        }

                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                        if(tcpAsyncTask3!=null){
                        	tcpAsyncTask3.stopTCPTask();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Waiting...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(false);*/

        }

        @Override
        protected List<IEsptouchResult> doInBackground(Object... params) {



            int taskResultCount = 1;
            synchronized (mLock) {
                String ssid = (String) params[0];//路由器ssid
                String password = (String) params[1];//路由器password

                byte[] userData = (byte[]) null; //用户数据
                byte deviceType = (byte) 0x09; //设备类型
                System.out.println("------------------AQUI-----------------------");
                System.out.println(ssid);
                System.out.println(password );
                mEsptouchTask = new EsptouchTask(ssid, password,
                        userData, deviceType,
                        cordova.getActivity());
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }


        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {

            //mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            //        .setEnabled(true);
            //mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
            //        "Confirm");

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                System.out.println("------------entre a onPostExecute ----------");
                if (firstResult.isSuc()) {

                    Log.i(TAG,"received multicast datagram from EG003 device data="+ BytesUtil.get16FromBytes(firstResult.getUserData()));


                    //resolve the byte array data from  eg003 device,need to  see the protocol  0x0001
                    BytesIO io = new BytesIO(firstResult.getUserData());

                    //jump to 18 position
                    io.getBytes(18);

                    //did is in relation ids,did is the device id
                    int did = io.getInt();

                    //jump to contnent
                    io.getBytes(18);


                    //device recovery version
                    int recoveryVersion = io.getShort();

                    //major version must be 0x09
                    int majorVersion = io.get()&0xFF;
                    //minor version must be 0x02
                    int minorVersion  =  io.get()&0xFF;

                    //the ip of the device
                    String ip = io.getIPString();

                    //the mac of the device
                    String macString = io.getMacString();

                    //the special of device ,now is useless
                    byte[] deviceSpecial = io.getBytes(8);

                    //the status of the device
                    int configFlag = io.getInt();


                    int len = io.getShort();

                    //the device status ,is lower battery
                    byte[] deviceState = io.getBytes(len);
                    
                    //EGetDevice eGetDevice = new EGetDevice();
                    eGetDevice.setDid(did);
                    eGetDevice.setIp(ip);
                    eGetDevice.setDmac(macString);
                    
                    //there set the markings use fix data,you can set you own data which you custom
                    //eGetDevice.setUserMarking("1");
                    //eGetDevice.setOrderMarking("16");
                    //eGetDevice.setDeviceName("eg003");
                    
                    tcpAsyncTask3 = new TCPAsyncTask3(this);
                    tcpAsyncTask3.execute(eGetDevice);
                    Log.v("----EG003----",eGetDevice.toString());

                } else {
                    System.out.println("------------entre a onPostExecute ----------");
                    PluginResult resultUDP = new PluginResult(PluginResult.Status.ERROR, "Esptouch fail");
                    resultUDP.setKeepCallback(true);           // keep callback after this call
                    receivingCallbackContext.sendPluginResult(resultUDP);
                    //mProgressDialog.setMessage("Esptouch fail");
                    Log.e(TAG,"set up failed ,did not received any data from EGO003");
                }
            }
        }
    }

    /**
     * 
     * the task of when receive the datagram from device,then use tcp connection to 
     * send ordermarking、userMarking、deviceName to device,when the device receive
     * the data,the configuration is succeeded!
     *
     */
    private class TCPAsyncTask3 extends AsyncTask<Object, Void, EGetDevice> {
    	/**
    	 * the task which establish a TCP connection 
    	 */
        private TCPSetupTask mTCPSetupTask;
        /**
         * the task which is TCPAsyncTask3 excute in
         */
        private EsptouchAsyncTask3 esptouchAsyncTask3;
        
        private final Object mLock = new Object();
        
        
        public TCPAsyncTask3(EsptouchAsyncTask3 esptouchAsyncTask3) {
			this.esptouchAsyncTask3 = esptouchAsyncTask3;
		}
        
		public void stopTCPTask() {
            synchronized (mLock) {
                if (__IEsptouchTask.DEBUG) {
                    Log.i(TAG, "TCP config is canceled");
                }
                if (mTCPSetupTask != null) {
                    mTCPSetupTask.interrupt();
                }
            }
        }

        @Override
        protected EGetDevice doInBackground(Object... params) {
        	
            synchronized (mLock) {
                EGetDevice device = (EGetDevice) params[0];
                mTCPSetupTask = new TCPSetupTask(device,
                        cordova.getActivity());
          
            }
            EGetDevice  eGetDevice = mTCPSetupTask.executeForResult();
            return eGetDevice;
        }


        /**
         * the device is which configed ,the important info are contains in the model
         */
        @Override
        protected void onPostExecute(final EGetDevice device) {
            System.out.println("entre onPostExecute");
        	cordova.getThreadPool().execute( new Runnable() {
				
				@Override
				public void run() {
					//config
		        	if(esptouchAsyncTask3!=null){
		        		esptouchAsyncTask3.stopEsptouchTask();
		        	}
		        	//if the device return is mean that setup failure
		        	if(device==null||TextUtils.isEmpty(device.getFirmwareMarking())){
		        		System.out.println("FALLO");
                        //Toast.makeText(cordova.getActivity(),"Set up failure,the device mac is "+device.getDmac(), Toast.LENGTH_LONG).show();
                        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Esptouch fail");
                        result.setKeepCallback(true);           // keep callback after this call
                        receivingCallbackContext.sendPluginResult(result);
		        		return;
		        	}
		        	
					//the device which you setup ,you can get all message from this device
                    System.out.println(":) Bien");
		        	//Toast.makeText(cordova.getActivity(),"Set up success,the device mac is "+device.getDmac(), Toast.LENGTH_LONG).show();
		        	Log.e(TAG, device.toString());
                    PluginResult result = new PluginResult(PluginResult.Status.OK, eGetDevice.getDmac().toString());
                    result.setKeepCallback(true);           // keep callback after this call
                    receivingCallbackContext.sendPluginResult(result);
				}
			});
        
        }
    }
}
