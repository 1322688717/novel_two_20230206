package cc.ixcc.noveltwo.jsReader.model.local;


import cc.ixcc.noveltwo.jsReader.model.bean.AuthorBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookCommentBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookHelpfulBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookHelpsBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookReviewBean;
import cc.ixcc.noveltwo.jsReader.model.bean.ReviewBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface DeleteDbHelper {
    void deleteBookComments(List<BookCommentBean> beans);
    void deleteBookReviews(List<BookReviewBean> beans);
    void deleteBookHelps(List<BookHelpsBean> beans);
    void deleteAuthors(List<AuthorBean> beans);
    void deleteBooks(List<ReviewBookBean> beans);
    void deleteBookHelpful(List<BookHelpfulBean> beans);
    void deleteAll();
}
