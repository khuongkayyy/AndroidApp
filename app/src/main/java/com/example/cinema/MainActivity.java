package com.example.cinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    // declaration
    private Button account;
    private Button ticketBtn,movieBtn,giftBtn,discountBtn;
    private  Button film1,film2,film3,film4,film5,film6,film7;

    private ImageView movieImage1,movieImage2,movieImage3,movieImage4,movieImage5,movieImage6,movieImage7;
    private ImageView apploadImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        sidebarButtonClicked();
        movieImageClick();
        movieButtonClick();
        accountSetting();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                TranslateAnimation animation = new TranslateAnimation(0.0f, 500.0f, 0.0f, 0.0f);
//                animation.setDuration(1000);
//                animation.setFillAfter(true);
//                apploadImg.startAnimation(animation);
//                apploadImg.setVisibility(View.GONE);
//            }
//        }, 3000);
    }

    private void accountSetting() {
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SettingMenu.class);
                startActivity(intent);
            }
        });
    }
    private void movieButtonClick() {
        setMovieButtonClickListener(film1,1);
        setMovieButtonClickListener(film2,2);
        setMovieButtonClickListener(film3,3);
        setMovieButtonClickListener(film4,4);
        setMovieButtonClickListener(film5,5);
        setMovieButtonClickListener(film6,6);
        setMovieButtonClickListener(film7,7);
    }

    private void setMovieButtonClickListener(Button button, int movieId) {
        TextView tempName = null;
        switch (movieId){
            case 1:
                tempName = findViewById(R.id.txtFilm1);
                break;
            case 2:
                tempName = findViewById(R.id.txtFilm2);
                break;
            case 3:
                tempName = findViewById(R.id.txtFilm3);
                break;
            case 4:
                tempName = findViewById(R.id.txtFilm4);
                break;
            case 5:
                tempName = findViewById(R.id.txtFilm5);
                break;
            case 6:
                tempName = findViewById(R.id.txtFilm6);
                break;
            case 7:
                tempName = findViewById(R.id.txtFilm7);
                break;
            default:
                return;
        }
        TextView finalTempName = tempName;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filmName = finalTempName.getText().toString();
                Intent intent = new Intent(MainActivity.this, MovieBook.class);
                intent.putExtra("filmName", filmName);
                startActivity(intent);
            }
        });
    }

    private void movieImageClick() {
        setMovieImageClickListener(movieImage1,1);
        setMovieImageClickListener(movieImage2,2);
        setMovieImageClickListener(movieImage3,3);
        setMovieImageClickListener(movieImage4,4);
        setMovieImageClickListener(movieImage5,5);
        setMovieImageClickListener(movieImage6,6);
        setMovieImageClickListener(movieImage7,7);
    }

    private void setMovieImageClickListener(ImageView movieImage, int movieId) {
        TextView tempName = null;
        switch (movieId){
            case 1:
                tempName = findViewById(R.id.txtFilm1);
                break;
            case 2:
                tempName = findViewById(R.id.txtFilm2);
                break;
            case 3:
                tempName = findViewById(R.id.txtFilm3);
                break;
            case 4:
                tempName = findViewById(R.id.txtFilm4);
                break;
            case 5:
                tempName = findViewById(R.id.txtFilm5);
                break;
            case 6:
                tempName = findViewById(R.id.txtFilm6);
                break;
            case 7:
                tempName = findViewById(R.id.txtFilm7);
                break;
            default:
                return;
        }
        TextView finalTempName = tempName;
        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filmName = finalTempName.getText().toString();
                Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                intent.putExtra("filmName", filmName);
                startActivity(intent);
            }
        });
    }

    private void sidebarButtonClicked() {
        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilmListScreen();
            }
        });
        giftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGiftScreen();
            }
        });
        discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiscountScreen();
            }
        });
        ticketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookTicketScreen();
            }
        });
    }

    private void initVariable() {
        //menu button:
        account = findViewById(R.id.btnAccountInfor);
        //sidebar button
        ticketBtn = findViewById(R.id.btnBookTicket);
        movieBtn = findViewById(R.id.btnFilmList);
        giftBtn = findViewById(R.id.btnGIft);
        discountBtn = findViewById(R.id.btnDiscount);
        //current movie ticket book
        film1 = findViewById(R.id.btnBookFilm1);
        film2 = findViewById(R.id.btnBookFilm2);
        film3 = findViewById(R.id.btnBookFilm3);
        film4 = findViewById(R.id.btnBookFilm4);
        film5 = findViewById(R.id.btnBookFilm5);
        film6 = findViewById(R.id.btnBookFilm6);
        film6 = findViewById(R.id.btnBookFilm7);
        film7 = findViewById(R.id.btnBookFilm7);
        //movie image
        movieImage1 = findViewById(R.id.movie1Img);
        movieImage2 = findViewById(R.id.movie2Img);
        movieImage3 = findViewById(R.id.movie3Img);
        movieImage4 = findViewById(R.id.movie4Img);
        movieImage5 = findViewById(R.id.movie5Img);
        movieImage6 = findViewById(R.id.movie6Img);
        movieImage7 = findViewById(R.id.movie7Img);
        apploadImg = findViewById(R.id.imgLoadApp);
    }

    private void openGiftScreen() {
        Intent intent = new Intent(MainActivity.this,GiftActivity.class);
        startActivity(intent);
    }

    private void openFilmListScreen() {
        Intent intent = new Intent(MainActivity.this,MovieList.class);
        startActivity(intent);
    }

    private void openBookTicketScreen() {
        Intent intent = new Intent(MainActivity.this,MovieBook.class);
        startActivity(intent);
    }

    private void openDiscountScreen() {
        Intent intent = new Intent(MainActivity.this,DiscountActivity.class);
        startActivity(intent);
    }
}