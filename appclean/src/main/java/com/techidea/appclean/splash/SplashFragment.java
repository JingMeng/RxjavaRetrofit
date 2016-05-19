package com.techidea.appclean.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techidea.appclean.R;
import com.techidea.appclean.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by zchao on 2016/5/18.
 */
public class SplashFragment extends BaseFragment implements SplashContract.View {

    private SplashContract.Presenter mPresenter;
    private Context mContext;

    public SplashFragment() {
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash,container,false);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Override
    public void showError(String message) {
        showToastMessage(message);
    }

    @Override
    protected void showToastMessage(String message) {
        super.showToastMessage(message);
    }

    @Override
    public void refreshProgress(int progress) {

    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getApplicationContext() {
        return mContext.getApplicationContext();
    }
}