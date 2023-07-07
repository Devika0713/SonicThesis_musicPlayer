package com.example.sonicthesis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class songInterface extends AppCompatActivity {

    ImageView play, prev, next, imageView, heart, shuffle, repeat;
    Button close;
    TextView songTitle;
    SeekBar mSeekBarTime, mSeekBarVol;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;

    // Custom class to hold song information
    class Song {
        private int songId;
        private String title;
        private int imageId;

        public Song(int songId, String title, int imageId) {
            this.songId = songId;
            this.title = title;
            this.imageId = imageId;
        }

        public int getSongId() {
            return songId;
        }

        public String getTitle() {
            return title;
        }

        public int getImageId() {
            return imageId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_interface2);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // initializing views
        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        songTitle = findViewById(R.id.songTitle);
        imageView = findViewById(R.id.imageView);
        heart = findViewById(R.id.heart);
        shuffle = findViewById(R.id.shuffle);
        repeat = findViewById(R.id.repeat);
        close = findViewById(R.id.close);

        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);
        shuffle.setClickable(true);
        repeat.setClickable(true);

        // creating an ArrayList to store songs
        final ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song(R.raw.kesariya, "Kesariya", R.drawable.kesariya));
        songs.add(new Song(R.raw.doubletake, "Double take", R.drawable.doubletake));
        songs.add(new Song(R.raw.raatanlambiya, "Raatan Lambiya", R.drawable.raatanlambiya));
        songs.add(new Song(R.raw.dusktilldawn, "Dusk till dawn", R.drawable.dusktilldawn));
        songs.add(new Song(R.raw.pyaarhote, "Pyaar hota hota kahi baar hai", R.drawable.pyaarhota));
        songs.add(new Song(R.raw.dandelions, "Dandelions", R.drawable.dandelions));
        // intializing mediaplayer
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(songs);
                currentIndex = 0; // Reset the current index to start from the first shuffled song

                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());
                mMediaPlayer.start();

                songTitle.setText(songs.get(currentIndex).getTitle());
                imageView.setImageResource(songs.get(currentIndex).getImageId());
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heart.setImageResource(R.drawable.heartfill);
            }
        });

        heart.setOnLongClickListener(view -> {
            heart.setImageResource(R.drawable.heart);
            return true;
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(songInterface.this, ThankYou.class);
                startActivity(i);
                mMediaPlayer.stop();
            }
        });
// not working because on completion is inside
//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSeekBarTime.setMax(mMediaPlayer.getDuration());
//                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                        mMediaPlayer.pause();
//                        play.setImageResource(R.drawable.play);
//                    } else {
//                        mMediaPlayer.start();
//                        play.setImageResource(R.drawable.pause);
//                    }
//
//                    if (repeat.isPressed()) {
//                        mMediaPlayer.setLooping(true);
//                    }
//
//                    songTitle.setText(songs.get(currentIndex).getTitle());
//                    imageView.setImageResource(songs.get(currentIndex).getImageId());
//            }
//        });

//not working
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                } else {
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }

                songTitle.setText(songs.get(currentIndex).getTitle());
                imageView.setImageResource(songs.get(currentIndex).getImageId());
            }
        });

// Set the onCompletionListener separately
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (repeat.isPressed()) {
                    mMediaPlayer.seekTo(0); // Rewind to the beginning of the song
                    mMediaPlayer.start();
                } else {
                    if (currentIndex == songs.size() - 1) {
                        currentIndex = 0; // Reset the index to play the first song
                    } else {
                        currentIndex++; // Move to the next song
                    }

                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.start();
                    songTitle.setText(songs.get(currentIndex).getTitle());
                    imageView.setImageResource(songs.get(currentIndex).getImageId());
                }
            }
        });



//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSeekBarTime.setMax(mMediaPlayer.getDuration());
//                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                    mMediaPlayer.pause();
//                    play.setImageResource(R.drawable.play);
//                } else {
//                    if (repeat.isPressed()) {
//                        mMediaPlayer.setLooping(true);
//                    } else {
//                        mMediaPlayer.setLooping(false);
//                        if (currentIndex == songs.size() - 1) {
//                            // If it's the last song, reset the index to play the first song
//                            currentIndex = 0;
//                        } else {
//                            // Otherwise, move to the next song
//                            currentIndex++;
//                        }
//                        if (mMediaPlayer.isPlaying()) {
//                            mMediaPlayer.stop();
//                        }
//                        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());
//                        mMediaPlayer.start();
//                        songTitle.setText(songs.get(currentIndex).getTitle());
//                    }
//                    play.setImageResource(R.drawable.pause);
//                }
//            }
//        });

//original play method
//        play.setOnClickListener(new View.OnClickListener() {
//           @Override
//            public void onClick(View v) {
//               mSeekBarTime.setMax(mMediaPlayer.getDuration());
//               if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                   mMediaPlayer.pause();
//                   play.setImageResource(R.drawable.play);
//               } else {
//                   mMediaPlayer.start();
//                   play.setImageResource(R.drawable.pause);
//               }
//               songTitle.setText(songs.get(currentIndex).getTitle());
//               imageView.setImageResource(songs.get(currentIndex).getImageId());
//           }
//        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.pause);
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());
                mMediaPlayer.start();

                songTitle.setText(songs.get(currentIndex).getTitle());
                imageView.setImageResource(songs.get(currentIndex).getImageId());
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.pause);
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex).getSongId());
                mMediaPlayer.start();

                songTitle.setText(songs.get(currentIndex).getTitle());
                imageView.setImageResource(songs.get(currentIndex).getImageId());
            }
        });

        // above seekbar volume
        //
        mSeekBarVol.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mSeekBarVol.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // Rest of your code (seekbar duration, thread for updating seekbar progress, etc.)
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
           public void run() {
                while (mMediaPlayer != null) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();}
                }
            }
        }).start();
    }
    public void close(View v){
        Intent i = new Intent(this,ThankYou.class);
        startActivity(i);
        mMediaPlayer.stop();
    }

    @SuppressLint("Handler Leak") Handler handler = new Handler () {
        @Override
        public void handleMessage  (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };
}

    // Rest of your code (handler, close method, etc.)



//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            mSeekBarTime.setProgress(msg.what);
//        }
//    };
//}
