package com.techidea.data.net;

import android.text.TextUtils;

import com.techidea.data.entity.ProductCategoryEntity;
import com.techidea.data.entity.ProductEntity;
import com.techidea.data.entity.UserInfoEntity;
import com.techidea.domain.LoginUser;
import com.techidea.domain.Product;
import com.techidea.domain.ProductCategory;
import com.techidea.domain.UserInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * Created by zchao on 2016/5/5.
 */
public class HttpMethods {

    private static final String BASE_URL = "";
    private static final String BASE_URL_HTTPS = "https://kyfw.12306.cn/";
    private static final int DEFAULT_TIMEOUT = 5;


    private Retrofit retrofit;
    private Retrofit retrofitHttps;
    private HttpApi service;
    private HttpApi serviceHttps;

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(CustomTrust.getInstance().getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

//        retrofitHttps = new Retrofit.Builder()
//                .client(CustomTrust.getInstance().getClientHttps())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(BASE_URL_HTTPS)
//                .build();

        service = retrofit.create(HttpApi.class);
//        serviceHttps = retrofitHttps.create(HttpApi.class);
    }

    public Observable<List<UserInfo>> initLoginUsers(String deviceId, String deviceType) {
        return service.initLoginUsers(deviceId, deviceType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFuncList<List<UserInfo>>());
    }

    public Observable<List<ProductCategory>> initProductCategory(String deviceId, String deviceType) {
        return service.initProductCategory(deviceId, deviceType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFuncList<List<ProductCategory>>());
    }

    public Observable<List<Product>> initProduct(String deviceId, String deviceType) {
        return service.initProduct(deviceId, deviceType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFuncList<List<Product>>());
    }

    public Observable<LoginUser> login(String deviceId, String username, String password) {
        return service.login(deviceId, username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFuncObject<LoginUser>());
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     * @return list
     */
    private class HttpResultFuncList<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() != 1)
                if (!TextUtils.isEmpty(httpResult.getMsg())) {
                    throw new HttpErrorException(httpResult.getMsg());
                } else {
                    throw new HttpErrorException(httpResult.getMsg());
                }
            return httpResult.getList();
        }
    }

    /**
     * @param <T>
     * @return object
     */
    private class HttpResultFuncObject<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() != 1) {
                if (!TextUtils.isEmpty(httpResult.getMsg())) {
                    throw new HttpErrorException(httpResult.getMsg());
                } else {
                    throw new HttpErrorException(httpResult.getMsg());
                }
            }
            return httpResult.getObject();
        }
    }
}
