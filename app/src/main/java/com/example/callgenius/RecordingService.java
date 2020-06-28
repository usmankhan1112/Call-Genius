package com.example.callgenius;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecordingService extends Service {
    public RecordingService() {
    }
    static MediaRecorder recorder = new MediaRecorder();
    File audiofile;

    String name, phonenumber;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    //private CallBr br_call;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "Destroy");
        super.onDestroy();
        stopRecord();
        getApplication().unregisterReceiver(receiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        //this.br_call = new CallBr();
        getApplication().registerReceiver(receiver, filter);

        return START_STICKY;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    private boolean startMediaRecorder(){
        try {
            recorder.reset();
            setAudioSource();
            recorder.setAudioSamplingRate(8000);
            recorder.setAudioEncodingBitRate(12200);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            String date = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
            String filePath = getFilesDir()+ "/Recordings" + File.separator + "Recording-" + date +".3gpp";
            File file = new File(filePath);

            if (!file.exists()) {
                file.mkdirs();
            }
            recorder.setOutputFile(file);
            recorder.prepare();

            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);

            recorder.start();

            return true;
        }
        catch (Exception e){
            e.getMessage();
            return false;
        }
    }

    public static void setAudioSource() {
        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            return;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            return;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            return;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        if (recorder != null) {
            //free record resource
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            Log.d(TAG, "Media recorder has been released");
        }
    }
*/
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        Bundle bundle;
        String state;
        String inCall, outCall;
        public boolean wasRinging = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);

                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        wasRinging = true;
                        //Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
                        Log.d("Incoming Call", "Incoming :" + inCall);
                    }
                    else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        if (wasRinging == true) {
                            //Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();
                            Log.d("Call Received", "Starting Record");
                            startAndSaveRecord();
                        }
                    }
                    else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        Toast.makeText(context, "REJECTED!", Toast.LENGTH_LONG).show();
                        Log.d("Call Rejected", "IN :" + inCall);
                        if (recordstarted) {
                            stopRecord();
                            recordstarted = false;
                        }
                    }
                }
            } else if (intent.getAction().equals(ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
                    Log.d("Outgoing Call", "Outgoing :" + inCall);
                    startAndSaveRecord();
                }
            }
        }

        void startAndSaveRecord(){
            String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
            File sampleDir = new File(getFilesDir(), "/Recordings");
            if (!sampleDir.exists()) {
                sampleDir.mkdir();
            }
            String file_name = "Record" + out;
            try {
                audiofile = File.createTempFile(file_name, ".amr", sampleDir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            recorder = new MediaRecorder();
            recorder.reset();

            //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();
            recordstarted = true;
        }
    };

    private void stopRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            Log.d(TAG, "Media recorder has been released");

        }
    }
}
