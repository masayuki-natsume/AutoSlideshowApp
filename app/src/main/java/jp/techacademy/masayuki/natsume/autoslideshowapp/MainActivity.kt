package jp.techacademy.masayuki.natsume.autoslideshowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity()  {

    private val PERMISSIONS_REQUEST_CODE = 100 //ﾊﾟｰﾐｯｼｮﾝｺｰﾄﾞ

    private var mTimer: Timer? = null

    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合(CODES.M以降の時)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する、権限付与をﾘｸｴｽﾄ
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    //ﾊﾟｰﾐｯｼｮﾝｺｰﾄﾞの承諾結果を受け取りﾒｯｿｯﾄﾞへ
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }else{
                    val str = "同意してください"
                    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                }
        }
    }

    // 画像の内容情報を取得する　contentResolver(格納ﾃﾞｰﾀを参照するｸﾗｽ)
    private fun getContentsInfo() {
        val resolver = contentResolver
        //ｶｰｿﾙ=条件のﾃﾞｰﾀﾃｰﾌﾞﾙ
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類指定
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

        playstop_button.setOnClickListener {
            if (mTimer == null) {
                mTimer = Timer()

                playstop_button.text = "停止"

                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mHandler.post {
                            //進むﾎﾞﾀﾝと同じ処理を記述
                            if (cursor!!.moveToNext()) {
                                // indexからIDを取得し、そのIDから画像のURIを取得する
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                imageView.setImageURI(imageUri)

                            } else if (cursor.moveToFirst()) {
                                // indexからIDを取得し、そのIDから画像のURIを取得する
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 100, 2000) // 最初に始動させるまで 100ミリ秒、ループの間隔を 100ミリ秒 に設定

                start_button.isClickable = false
                back_button.isClickable = false

            }else {
                mTimer!!.cancel() //停止の処理
                mTimer = null

                playstop_button.text = "再生"
                start_button.isClickable = true
                back_button.isClickable = true
            }
        }

        start_button.setOnClickListener {
            if (cursor!!.moveToNext()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                imageView.setImageURI(imageUri)

            } else if (cursor.moveToFirst()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                imageView.setImageURI(imageUri)
            }
        }

        back_button.setOnClickListener {
            if (cursor!!.moveToPrevious()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                imageView.setImageURI(imageUri)

            } else if (cursor.moveToLast()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                imageView.setImageURI(imageUri)
            }
        }
    }
}
