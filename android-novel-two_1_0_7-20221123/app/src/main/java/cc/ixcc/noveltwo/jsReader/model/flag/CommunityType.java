package cc.ixcc.noveltwo.jsReader.model.flag;


import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.treader.AppContext;

/**
 * Created by newbiechen on 17-4-24.
 */

public enum CommunityType {

    COMMENT(R.string.nb_fragment_community_comment, "ramble",R.drawable.ic_section_comment),
    REVIEW(R.string.nb_fragment_community_discussion,"",R.drawable.ic_section_discuss),
    HELP(R.string.nb_fragment_community_help,"",R.drawable.ic_section_help),
    GIRL(R.string.nb_fragment_community_girl,"girl",R.drawable.ic_section_girl),
    COMPOSE(R.string.nb_fragment_community_compose,"original",R.drawable.ic_section_compose);

    private String typeName;
    private String netName;
    private int iconId;
    CommunityType(@StringRes int typeId, String netName, @DrawableRes int iconId){
        this.typeName = AppContext.sInstance.getResources().getString(typeId);
        this.netName = netName;
        this.iconId = iconId;
    }

    public String getTypeName(){
        return typeName;
    }

    public String getNetName(){
        return netName;
    }

    public int getIconId(){
        return iconId;
    }
}
