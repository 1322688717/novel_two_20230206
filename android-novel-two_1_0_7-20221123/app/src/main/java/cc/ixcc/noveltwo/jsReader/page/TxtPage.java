package cc.ixcc.noveltwo.jsReader.page;

import java.util.List;

/**
 * Created by newbiechen on 17-7-1.
 */
//页面
public class TxtPage {
    int position;
    String title;
    //当前 lines 中为 title 的行数。
    int titleLines;
    List<String> lines;
    boolean hasPay;
    boolean isAd;
}
