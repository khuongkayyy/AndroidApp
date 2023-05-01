package com.example.cinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    // declaration
    private Button ticketBtn,movieBtn,giftBtn,discountBtn;
    private  Button film1,film2,film3,film4,film5,film6,film7;

    private ImageView movieImage1,movieImage2,movieImage3,movieImage4,movieImage5,movieImage6,movieImage7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        sidebarButtonClicked();
        movieImageClick();
//        buttonClick();
//        film1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,MovieBook.class);
//                TextView temp = findViewById(R.id.txtFilm1);
//                String temp1 = temp.getText().toString();
//                intent.putExtra("filmName",temp1);
//                startActivity(intent);
//            }
//        });
    }
    private void buttonClick(){
        setButtonClickListener(film1,1);
        setButtonClickListener(film2,2);
        setButtonClickListener(film3,3);
        setButtonClickListener(film4,4);
        setButtonClickListener(film5,5);
        setButtonClickListener(film6,6);
        setButtonClickListener(film7,7);
    }
    private void setButtonClickListener(Button button, int btnId){
        TextView tempTv = null;
//        String tempStr = null;
        switch (btnId){
            case 1:
                tempTv = findViewById(R.id.txtFilm1);
//                tempStr = tempTv.getText().toString();
                break;
            case 2:
                tempTv = findViewById(R.id.txtFilm2);
//                tempStr = tempTv.getText().toString();
                break;
            case 3:
                tempTv = findViewById(R.id.txtFilm3);
//                tempStr = tempTv.getText().toString();
                break;
            case 4:
                tempTv = findViewById(R.id.txtFilm4);
//                tempStr = tempTv.getText().toString();
                break;
            case 5:
                tempTv = findViewById(R.id.txtFilm5);
//                tempStr = tempTv.getText().toString();
                break;
            case 6:
                tempTv = findViewById(R.id.txtFilm6);
//                tempStr = tempTv.getText().toString();
                break;
            case 7:
                tempTv = findViewById(R.id.txtFilm7);
//                tempStr = tempTv.getText().toString();
                break;
            default:
                return;
        }
        TextView finalTempName = tempTv;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filmName = finalTempName.getText().toString();
                Intent intent = new Intent(MainActivity.this,MovieBook.class);
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
        TextView tempTime  = null;
        switch (movieId){
            case 1:
                tempName = findViewById(R.id.txtFilm1);
                tempTime = findViewById(R.id.txtFilm1_Time);
                break;
            case 2:
                tempName = findViewById(R.id.txtFilm2);
                tempTime = findViewById(R.id.txtFilm2_Time);
                break;
            case 3:
                tempName = findViewById(R.id.txtFilm3);
                tempTime = findViewById(R.id.txtFilm3_Time);
                break;
            case 4:
                tempName = findViewById(R.id.txtFilm4);
                tempTime = findViewById(R.id.txtFilm4_Time);
                break;
            case 5:
                tempName = findViewById(R.id.txtFilm5);
                tempTime = findViewById(R.id.txtFilm5_Time);
                break;
            case 6:
                tempName = findViewById(R.id.txtFilm6);
                tempTime = findViewById(R.id.txtFilm6_Time);
                break;
            case 7:
                tempName = findViewById(R.id.txtFilm7);
                tempTime = findViewById(R.id.txtFilm7_Time);
                break;
            default:
                return;
        }
        TextView finalTempName = tempName;
//        TextView finalTempTime = tempTime;
        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filmName = finalTempName.getText().toString();
//                String filmTime = finalTempTime.getText().toString();
//                int dividerIndex = filmTime.indexOf(" | ");
//                String durationString = filmTime.substring(0, dividerIndex).toLowerCase();
//                String dateString = filmTime.substring(dividerIndex + 3);
                Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                intent.putExtra("filmName", filmName);
//                intent.putExtra("filmDate", dateString);
//                intent.putExtra("filmDuration", durationString);
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
        //movie image
        movieImage1 = findViewById(R.id.movie1Img);
        movieImage2 = findViewById(R.id.movie2Img);
        movieImage3 = findViewById(R.id.movie3Img);
        movieImage4 = findViewById(R.id.movie4Img);
        movieImage5 = findViewById(R.id.movie5Img);
        movieImage6 = findViewById(R.id.movie6Img);
        movieImage7 = findViewById(R.id.movie7Img);
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