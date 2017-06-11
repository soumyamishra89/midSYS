package digitalhealth.de.midsys.fragment;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 11/06/17.
 */

public class FragmentTutorial extends Fragment {

    private MediaController mediaControls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_tutorial, container, false);
        Log.i("FragmentTutorial", "onCreateView");
        root.findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i("FragmentTutorial", "Video OnClickListener");
                final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.video_upload_layout);

                VideoView videoView = (VideoView) dialog.findViewById(R.id.upload_video);

                videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video);

                FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.controllerAnchor);
                videoView.requestFocus();
                setVideoView(frameLayout, videoView);

                dialog.findViewById(R.id.upload_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        dialog.dismiss();
                        //popupWindow.dismiss();
                    }
                });
                dialog.show();

            }
        });

        return root;
    }

    private void setVideoView(FrameLayout frameLayout, final VideoView videoView){
        if (mediaControls == null) {
            mediaControls = new MediaController(getContext()) {
                @Override
                public void hide(){
                }
            };
        }

        //mediaControls.setAnchorView(frameLayout);
        mediaControls.setMediaPlayer(videoView);
        videoView.setMediaController(mediaControls);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        mediaControls.setLayoutParams(lp);

        ( (ViewGroup) mediaControls.getParent() ).removeView(mediaControls);

        frameLayout.addView(mediaControls);
        //videoView.setVideoPath(videoPath);

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer){
                mediaControls.show();
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                mediaControls.hide();
            }
        });
    }
}
