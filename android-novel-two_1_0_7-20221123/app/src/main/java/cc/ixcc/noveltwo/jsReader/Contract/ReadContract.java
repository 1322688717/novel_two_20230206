package cc.ixcc.noveltwo.jsReader.Contract;


import cc.ixcc.noveltwo.jsReader.model.bean.BookChapterBean;
import cc.ixcc.noveltwo.jsReader.page.TxtChapter;

import java.util.List;

/**
 * Created by newbiechen on 17-5-16.
 */

public interface ReadContract extends BaseContract{
    interface View extends BaseContract.BaseView {
        void showCategory(List<BookChapterBean> bookChapterList);
        void finishChapter();
        void errorChapter();

        void upDate(String link);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadCategory(String bookId);
        void loadChapter(String bookId, List<TxtChapter> bookChapterList);

        void loadAd();
    }
}
