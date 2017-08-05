package org.ccccoder.rapbattle.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.ccccoder.rapbattle.R;
import org.ccccoder.rapbattle.Service.RecordingService;

import java.io.File;

public class RecordFragment extends Fragment {
    //Record
    static final String RECORDED_FILE = "/sdcard/recorded.mp4";
    MediaPlayer mp;
    MediaPlayer player;
    MediaRecorder recorder;
    int playbackPosition = 0;
    private Button mPlayButton = null;
    private boolean mStartPlaying = true;


    //Recording controls
    private FloatingActionButton mFab = null;
    private FloatingActionButton mRecordButton = null;
    private Button mPauseButton = null;

    private TextView mRecordingPrompt;
    private int mRecordPromptCount = 0;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;

    private Chronometer mChronometer = null;
    long timeWhenPaused = 0; //stores time when user clicks pause button


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View recordView = inflater.inflate(R.layout.fragment_record, container, false);
        /*RECORD*/
        mPlayButton = (Button)recordView.findViewById(R.id.play_btn);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartPlaying = !mStartPlaying;
                if(!mStartPlaying) play_call();
                else playstop_call();
            }
        });
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setVisibility(View.INVISIBLE);
        mChronometer = (Chronometer) recordView.findViewById(R.id.chronometer);//update recording prompt text
        mRecordingPrompt = (TextView) recordView.findViewById(R.id.recording_status_text);
        mRecordButton = (FloatingActionButton) recordView.findViewById(R.id.btnRecord);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
                if(!mStartRecording)
                {
                    record_call();
                }else{
                    recordstop_call();
                    mPlayButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mPauseButton = (Button) recordView.findViewById(R.id.btnPause);
        mPauseButton.setVisibility(View.GONE); //hide pause button before recording starts
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseRecord(mPauseRecording);
                mPauseRecording = !mPauseRecording;
            }
        });

        return recordView;
    }

    private void onRecord(boolean start){
       //////////////////// Intent intent = new Intent(getActivity(), RecordingService.class);
        if (start) {
            // start recording
            mRecordButton.setImageResource(R.drawable.record_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"Recording started",Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        mRecordingPrompt.setText("Recording" + ".");
                    } else if (mRecordPromptCount == 1) {
                        mRecordingPrompt.setText("Recording" + "..");
                    } else if (mRecordPromptCount == 2) {
                        mRecordingPrompt.setText("Recording" + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            //start RecordingService
         ////////////////////////////////   getActivity().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRecordingPrompt.setText("Recording" + ".");
            mRecordPromptCount++;

        } else {
            //stop recording
            mRecordButton.setImageResource(R.drawable.record_mic_white);
            //mPauseButton.setVisibility(View.GONE);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            mRecordingPrompt.setText("Tap the button to start recording");

         //////////////////////////   getActivity().stopService(intent);
            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void onPauseRecord(boolean pause) {
        if (pause)  //RECORD PAUSE
        {
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.record_play ,0 ,0 ,0);
            mRecordingPrompt.setText((String)getString(R.string.resume_recording_button).toUpperCase());
            timeWhenPaused = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
        } else      //RECORD RESUME
        {
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.record_pause ,0 ,0 ,0);
            mRecordingPrompt.setText((String)getString(R.string.pause_recording_button).toUpperCase());
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            mChronometer.start();
        }
    }

    public void record_call(){
        if(recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }// TODO Auto-generated method stub
        try{
            mp = MediaPlayer.create(
                    getActivity().getApplicationContext(), // 현재 화면의 제어권자
                    R.raw.chacha); // 음악파일
            mp.setLooping(false); // true:무한반복
            mp.start(); // 노래 재생 시작

            Toast.makeText(getActivity().getApplicationContext(), "음악파일 재생 시작됨.", Toast.LENGTH_LONG).show();
        } catch(Exception e){
            e.printStackTrace();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        //Set audio format, MPEG4 media file format, AMR NB file format, 3GPP media file format
        if (Build.VERSION.SDK_INT >= 10) {
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioEncodingBitRate(192000);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        } else {
            // older version of Android, use crappy sounding voice codec
            recorder.setAudioSamplingRate(8000);
            recorder.setAudioEncodingBitRate(12200);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
        recorder.setOutputFile(RECORDED_FILE);
        try{
            Toast.makeText(getActivity().getApplicationContext(),
                    "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
            recorder.prepare();
            recorder.start();
        }catch (Exception ex){
            Log.e("SampleAudioRecorder", "Exception : ", ex);
        }
    }

    public void recordstop_call(){
        if(recorder == null);else {
            mp.stop();
            mp.release();
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(getActivity().getApplicationContext(),
                    "녹음이 중지되었습니다.", Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
        }
    }

    public void play_call()
    {
        try{
            playAudio(RECORDED_FILE);
            Toast.makeText(getActivity().getApplicationContext(), "음악파일 재생 시작됨.", Toast.LENGTH_LONG).show();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void playstop_call()
    {
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            player.pause();
            Toast.makeText(getActivity().getApplicationContext(), "음악 파일 재생 중지됨.",Toast.LENGTH_LONG).show();
        }
    }
    private void playAudio(String url) throws Exception{
        killMediaPlayer();
        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }
    private void killMediaPlayer() {
        if(player != null){
            try {
                player.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onPause(){
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if (player != null){
            player.release();
            player = null;
        }
        super.onPause();
    }


}