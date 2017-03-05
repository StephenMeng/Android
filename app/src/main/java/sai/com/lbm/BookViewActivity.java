package sai.com.lbm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sai.com.lbm.scanbook.bean.Book;
import sai.com.lbm.scanbook.util.BookUtil;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * <a href="http://fangjie.sinaapp.com">http://fangjie.sinaapp.com</a>
 *
 * @author JayFang
 * @version 1.0
 * @describe 图书信息显示Activity
 */

public class BookViewActivity extends Activity implements OnClickListener {
    private Intent intent;
    private TextView tv_rate, tv_price, tv_title, tv_author, tv_publisher, tv_date, tv_isbn, tv_summary;
    private TextView tv_page, tv_tags;
    private static TextView tv_content;
    private TextView tv_authorinfo;
    private TextView tv_content_menu;
    private ImageView image;
    private ImageView arrow;
    private Button btn_back;
    private Button btn_addbook, btn_borrowbook, btn_returnbook;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setContentView(R.layout.bookview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.titlebar);

        tv_title = (TextView) findViewById(R.id.bookview_title);
        tv_author = (TextView) findViewById(R.id.bookview_author);
        tv_publisher = (TextView) findViewById(R.id.bookview_publisher);
        tv_date = (TextView) findViewById(R.id.bookview_publisherdate);
        tv_isbn = (TextView) findViewById(R.id.bookview_isbn);
        tv_summary = (TextView) findViewById(R.id.bookview_summary);
        tv_rate = (TextView) findViewById(R.id.bookview_rate);
        tv_price = (TextView) findViewById(R.id.bookview_price);
        tv_page = (TextView) findViewById(R.id.bookview_pages);
        tv_content = (TextView) findViewById(R.id.bookview_content);
        tv_tags = (TextView) findViewById(R.id.bookview_tag);
        tv_authorinfo = (TextView) findViewById(R.id.bookview_authorinfo);
        image = (ImageView) findViewById(R.id.bookview_cover);
        arrow = (ImageView) findViewById(R.id.bookview_arrow);
        btn_addbook = (Button) findViewById(R.id.btn_addbook);
        btn_borrowbook = (Button) findViewById(R.id.btn_borrowbook);
        btn_returnbook = (Button) findViewById(R.id.btn_returnbook);

        btn_addbook.setOnClickListener(this);
        btn_borrowbook.setOnClickListener(this);
        btn_returnbook.setOnClickListener(this);

        tv_content_menu = (TextView) findViewById(R.id.bookview_content_menu);
        tv_content_menu.setClickable(true);
        tv_content_menu.setFocusable(true);
        tv_content_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BookViewActivity.tv_content.getVisibility() == View.GONE) {
                    arrow.setImageResource(R.drawable.down);
                    BookViewActivity.tv_content.setVisibility(View.VISIBLE);
                } else {
                    arrow.setImageResource(R.drawable.right);
                    BookViewActivity.tv_content.setVisibility(View.GONE);
                }
            }
        });
        //豆瓣书评弹出TextView
        tv_content_menu = (TextView) findViewById(R.id.bookview_review);
        tv_content_menu.setClickable(true);
        tv_content_menu.setFocusable(true);
        tv_content_menu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(BookViewActivity.this, ReviewListActivity.class);
                intent2.putExtra("id", book.getId());
                intent2.putExtra("name", book.getTitle());
                startActivity(intent2);
            }
        });

        //返回按钮
        btn_back = (Button) findViewById(R.id.titlebar_bt_back);
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(BookViewActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        //获取从MainActivity中传来的Book
        intent = getIntent();
        book = (Book) intent.getParcelableExtra(Book.class.getName());

        //将Book信息显示在控件上
        if (book.getRate().equals("0.0"))
            tv_rate.setText("少于10人评价");
        else
            tv_rate.setText("评分:" + book.getRate() + "分");
        tv_title.setText(book.getTitle());
        tv_author.setText("作者:" + book.getAuthor());
        tv_publisher.setText("出版社:" + book.getPublisher());
        tv_date.setText("出版时间:" + book.getPublishDate());
        tv_isbn.setText("ISBN:" + book.getISBN());
        tv_summary.setText(book.getSummary());
        tv_page.setText("页数:" + book.getPage());
        tv_price.setText("定价:" + book.getPrice());
        tv_content.setText(book.getContent());

        tv_authorinfo.setText(book.getAuthorInfo());
        tv_tags.setText("标签:" + book.getTag());
        image.setImageBitmap(book.getBitmap());

    }

    @Override
    public void onClick(View v) {
        String url = null;
        switch (v.getId()) {
            case R.id.titlebar_bt_back:
                Intent intent1 = new Intent(BookViewActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_addbook:
                url = "http://192.168.1.102:8080/addbook.do?isbn=" + book.getISBN();
                new BookAsyncTask().execute(url);
                break;
            case R.id.btn_borrowbook:
                url = "http://192.168.1.102:8080/borrow.do?isbn=" + book.getISBN();
                new BookAsyncTask().execute(url);
                break;
            case R.id.btn_returnbook:
                url = "http://192.168.1.102:8080/return.do?isbn=" + book.getISBN();
                new BookAsyncTask().execute(url);
                break;
        }
    }

    class BookAsyncTask extends AsyncTask<String, Void, String> {

        public BookAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            if (BookUtil.getHttpRequest(params[0]) != null) {
                return "ok";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                Toast.makeText(BookViewActivity.this, "操作成功", LENGTH_SHORT).show();
            }
        }
    }
}
