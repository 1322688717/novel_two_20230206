package cc.ixcc.noveltwo.jsReader.model.local;



import cc.ixcc.noveltwo.jsReader.model.bean.AuthorBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookCommentBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookHelpfulBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookHelpsBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookReviewBean;
import cc.ixcc.noveltwo.jsReader.model.bean.DownloadTaskBean;
import cc.ixcc.noveltwo.jsReader.model.bean.ReviewBookBean;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface GetDbHelper {
    Single<List<BookCommentBean>> getBookComments(String block, String sort, int start, int limited, String distillate);
    Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limited, String distillate);
    Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate);
//    BookSortPackage getBookSortPackage();
//    BillboardPackage getBillboardPackage();

    AuthorBean getAuthor(String id);
    ReviewBookBean getReviewBook(String id);
    BookHelpfulBean getBookHelpful(String id);

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
