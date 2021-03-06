package io.playcode.streambox.ui.panda;

import java.util.ArrayList;
import java.util.List;

import io.playcode.streambox.data.bean.PandaStreamListEntity;
import io.playcode.streambox.data.source.AppRepository;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anpoz on 2017/4/13.
 */

public class PandaListPresenter implements PandaListContract.Presenter {
    private PandaListContract.View mView;
    private String gameType;
    private CompositeDisposable mCompositeDisposable;
    private int curPageNo;
    private List<PandaStreamListEntity.DataEntity.ItemsEntity> mPandaRoomEntities;

    public PandaListPresenter(PandaListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        curPageNo = 1;
        mPandaRoomEntities = new ArrayList<>();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @Override
    public void requestRefresh() {
        curPageNo = 1;
        AppRepository.getInstance()
                .getPandaStreamList(gameType, curPageNo + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PandaStreamListEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PandaStreamListEntity pandaStreamListEntity) {
                        mPandaRoomEntities.clear();
                        mPandaRoomEntities.addAll(pandaStreamListEntity.getData().getItems());
                        mView.update(mPandaRoomEntities);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void requestUpdate() {
        curPageNo++;
        AppRepository.getInstance()
                .getPandaStreamList(gameType, curPageNo + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PandaStreamListEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PandaStreamListEntity pandaStreamListEntity) {
                        mPandaRoomEntities.addAll(pandaStreamListEntity.getData().getItems());
                        mView.update(mPandaRoomEntities);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
