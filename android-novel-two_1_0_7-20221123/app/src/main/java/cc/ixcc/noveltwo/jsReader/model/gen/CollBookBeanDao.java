package cc.ixcc.noveltwo.jsReader.model.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cc.ixcc.noveltwo.jsReader.model.bean.CollBookBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COLL_BOOK_BEAN".
*/
public class CollBookBeanDao extends AbstractDao<CollBookBean, String> {

    public static final String TABLENAME = "COLL_BOOK_BEAN";

    /**
     * Properties of entity CollBookBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, String.class, "_id", true, "_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Author = new Property(2, String.class, "author", false, "AUTHOR");
        public final static Property ShareTitle = new Property(3, String.class, "shareTitle", false, "SHARE_TITLE");
        public final static Property ShortIntro = new Property(4, String.class, "shortIntro", false, "SHORT_INTRO");
        public final static Property CoverPic = new Property(5, String.class, "coverPic", false, "COVER_PIC");
        public final static Property Share_link = new Property(6, String.class, "share_link", false, "SHARE_LINK");
        public final static Property Cover = new Property(7, String.class, "cover", false, "COVER");
        public final static Property Classify = new Property(8, String.class, "classify", false, "CLASSIFY");
        public final static Property HasCp = new Property(9, boolean.class, "hasCp", false, "HAS_CP");
        public final static Property LatelyFollower = new Property(10, int.class, "latelyFollower", false, "LATELY_FOLLOWER");
        public final static Property RetentionRatio = new Property(11, double.class, "retentionRatio", false, "RETENTION_RATIO");
        public final static Property Updated = new Property(12, String.class, "updated", false, "UPDATED");
        public final static Property LastRead = new Property(13, String.class, "lastRead", false, "LAST_READ");
        public final static Property Paychapter = new Property(14, int.class, "paychapter", false, "PAYCHAPTER");
        public final static Property ChaptersCount = new Property(15, int.class, "chaptersCount", false, "CHAPTERS_COUNT");
        public final static Property LastChapter = new Property(16, String.class, "lastChapter", false, "LAST_CHAPTER");
        public final static Property IsUpdate = new Property(17, boolean.class, "isUpdate", false, "IS_UPDATE");
        public final static Property IsLocal = new Property(18, boolean.class, "isLocal", false, "IS_LOCAL");
        public final static Property Iswz = new Property(19, int.class, "iswz", false, "ISWZ");
        public final static Property Allchapter = new Property(20, int.class, "allchapter", false, "ALLCHAPTER");
    }

    private DaoSession daoSession;


    public CollBookBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CollBookBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COLL_BOOK_BEAN\" (" + //
                "\"_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: _id
                "\"TITLE\" TEXT," + // 1: title
                "\"AUTHOR\" TEXT," + // 2: author
                "\"SHARE_TITLE\" TEXT," + // 3: shareTitle
                "\"SHORT_INTRO\" TEXT," + // 4: shortIntro
                "\"COVER_PIC\" TEXT," + // 5: coverPic
                "\"SHARE_LINK\" TEXT," + // 6: share_link
                "\"COVER\" TEXT," + // 7: cover
                "\"CLASSIFY\" TEXT," + // 8: classify
                "\"HAS_CP\" INTEGER NOT NULL ," + // 9: hasCp
                "\"LATELY_FOLLOWER\" INTEGER NOT NULL ," + // 10: latelyFollower
                "\"RETENTION_RATIO\" REAL NOT NULL ," + // 11: retentionRatio
                "\"UPDATED\" TEXT," + // 12: updated
                "\"LAST_READ\" TEXT," + // 13: lastRead
                "\"PAYCHAPTER\" INTEGER NOT NULL ," + // 14: paychapter
                "\"CHAPTERS_COUNT\" INTEGER NOT NULL ," + // 15: chaptersCount
                "\"LAST_CHAPTER\" TEXT," + // 16: lastChapter
                "\"IS_UPDATE\" INTEGER NOT NULL ," + // 17: isUpdate
                "\"IS_LOCAL\" INTEGER NOT NULL ," + // 18: isLocal
                "\"ISWZ\" INTEGER NOT NULL ," + // 19: iswz
                "\"ALLCHAPTER\" INTEGER NOT NULL );"); // 20: allchapter
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COLL_BOOK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CollBookBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(3, author);
        }
 
        String shareTitle = entity.getShareTitle();
        if (shareTitle != null) {
            stmt.bindString(4, shareTitle);
        }
 
        String shortIntro = entity.getShortIntro();
        if (shortIntro != null) {
            stmt.bindString(5, shortIntro);
        }
 
        String coverPic = entity.getCoverPic();
        if (coverPic != null) {
            stmt.bindString(6, coverPic);
        }
 
        String share_link = entity.getShare_link();
        if (share_link != null) {
            stmt.bindString(7, share_link);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(8, cover);
        }
 
        String classify = entity.getClassify();
        if (classify != null) {
            stmt.bindString(9, classify);
        }
        stmt.bindLong(10, entity.getHasCp() ? 1L: 0L);
        stmt.bindLong(11, entity.getLatelyFollower());
        stmt.bindDouble(12, entity.getRetentionRatio());
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(13, updated);
        }
 
        String lastRead = entity.getLastRead();
        if (lastRead != null) {
            stmt.bindString(14, lastRead);
        }
        stmt.bindLong(15, entity.getPaychapter());
        stmt.bindLong(16, entity.getChaptersCount());
 
        String lastChapter = entity.getLastChapter();
        if (lastChapter != null) {
            stmt.bindString(17, lastChapter);
        }
        stmt.bindLong(18, entity.getIsUpdate() ? 1L: 0L);
        stmt.bindLong(19, entity.getIsLocal() ? 1L: 0L);
        stmt.bindLong(20, entity.getIswz());
        stmt.bindLong(21, entity.getAllchapter());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CollBookBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(3, author);
        }
 
        String shareTitle = entity.getShareTitle();
        if (shareTitle != null) {
            stmt.bindString(4, shareTitle);
        }
 
        String shortIntro = entity.getShortIntro();
        if (shortIntro != null) {
            stmt.bindString(5, shortIntro);
        }
 
        String coverPic = entity.getCoverPic();
        if (coverPic != null) {
            stmt.bindString(6, coverPic);
        }
 
        String share_link = entity.getShare_link();
        if (share_link != null) {
            stmt.bindString(7, share_link);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(8, cover);
        }
 
        String classify = entity.getClassify();
        if (classify != null) {
            stmt.bindString(9, classify);
        }
        stmt.bindLong(10, entity.getHasCp() ? 1L: 0L);
        stmt.bindLong(11, entity.getLatelyFollower());
        stmt.bindDouble(12, entity.getRetentionRatio());
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(13, updated);
        }
 
        String lastRead = entity.getLastRead();
        if (lastRead != null) {
            stmt.bindString(14, lastRead);
        }
        stmt.bindLong(15, entity.getPaychapter());
        stmt.bindLong(16, entity.getChaptersCount());
 
        String lastChapter = entity.getLastChapter();
        if (lastChapter != null) {
            stmt.bindString(17, lastChapter);
        }
        stmt.bindLong(18, entity.getIsUpdate() ? 1L: 0L);
        stmt.bindLong(19, entity.getIsLocal() ? 1L: 0L);
        stmt.bindLong(20, entity.getIswz());
        stmt.bindLong(21, entity.getAllchapter());
    }

    @Override
    protected final void attachEntity(CollBookBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public CollBookBean readEntity(Cursor cursor, int offset) {
        CollBookBean entity = new CollBookBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // author
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shareTitle
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shortIntro
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // coverPic
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // share_link
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // cover
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // classify
            cursor.getShort(offset + 9) != 0, // hasCp
            cursor.getInt(offset + 10), // latelyFollower
            cursor.getDouble(offset + 11), // retentionRatio
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // updated
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // lastRead
            cursor.getInt(offset + 14), // paychapter
            cursor.getInt(offset + 15), // chaptersCount
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // lastChapter
            cursor.getShort(offset + 17) != 0, // isUpdate
            cursor.getShort(offset + 18) != 0, // isLocal
            cursor.getInt(offset + 19), // iswz
            cursor.getInt(offset + 20) // allchapter
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CollBookBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAuthor(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setShareTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShortIntro(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCoverPic(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setShare_link(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCover(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setClassify(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setHasCp(cursor.getShort(offset + 9) != 0);
        entity.setLatelyFollower(cursor.getInt(offset + 10));
        entity.setRetentionRatio(cursor.getDouble(offset + 11));
        entity.setUpdated(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setLastRead(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setPaychapter(cursor.getInt(offset + 14));
        entity.setChaptersCount(cursor.getInt(offset + 15));
        entity.setLastChapter(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIsUpdate(cursor.getShort(offset + 17) != 0);
        entity.setIsLocal(cursor.getShort(offset + 18) != 0);
        entity.setIswz(cursor.getInt(offset + 19));
        entity.setAllchapter(cursor.getInt(offset + 20));
     }
    
    @Override
    protected final String updateKeyAfterInsert(CollBookBean entity, long rowId) {
        return entity.get_id();
    }
    
    @Override
    public String getKey(CollBookBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CollBookBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
