package com.gdut.library.makemake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.util.Log;

import com.gdut.library.makemake.apihelper.ApiHelper;
import com.gdut.library.makemake.apihelper.ApiUser;
import com.gdut.library.makemake.apihelper.ApiBook;
import com.gdut.library.makemake.R;

public class MakemakeActivity extends Activity {
    private final String TAG = "makemake";

    private TextView content;
    private EditText username;
    private EditText password;
    private Button login;
    private EditText ctrlno;
    private Button getBookButton;
    private EditText keyword;
    private Button search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        content = (TextView) findViewById(R.id.content);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        ctrlno = (EditText) findViewById(R.id.ctrlno);
        getBookButton = (Button) findViewById(R.id.getBook);
        keyword = (EditText) findViewById(R.id.keyword);
        search = (Button) findViewById(R.id.search);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login:
                        doLogin();
                        break;
                    case R.id.getBook:
                        getBook();
                        break;
                    case R.id.search:
                        search();
                        break;
                }
            }
        };
        login.setOnClickListener(onClickListener);
        getBookButton.setOnClickListener(onClickListener);
        search.setOnClickListener(onClickListener);
    }

    private void doLogin() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ApiHelper helper = new ApiHelper(username.getText().toString(),
                                                     password.getText().toString());
                    ApiUser user = helper.login();
                    Log.d(TAG, user.name);

                    ApiBook[] books = helper.getBooks();
                    Log.d(TAG, books.length + "");
                    books = helper.addBook("12345");
                    Log.d(TAG, books.length + "");
                    helper.removeBook("12345");
                    books = helper.getBooks();
                    Log.d(TAG, books.length + "");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        }.start();
    }

    private void getBook() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ApiHelper helper = new ApiHelper();
                    ApiBook book = helper.getBookByCtrlno(ctrlno.getText().toString());
                    Log.d(TAG, book.name);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        }.start();
    }

    private void search() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ApiHelper helper = new ApiHelper();
                    ApiBook[] books = helper.searchBooks(keyword.getText().toString());
                    Log.d(TAG, books.length + "");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        }.start();
    }
}
