package com.test.lsm.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.test.lsm.db.bean.PushMsg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PUSH_MSG".
*/
public class PushMsgDao extends AbstractDao<PushMsg, Long> {

    public static final String TABLENAME = "PUSH_MSG";

    /**
     * Properties of entity PushMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ImgId = new Property(1, Integer.class, "imgId", false, "IMG_ID");
        public final static Property ImgUrl = new Property(2, String.class, "imgUrl", false, "IMG_URL");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Datetime = new Property(4, String.class, "datetime", false, "DATETIME");
    }


    public PushMsgDao(DaoConfig config) {
        super(config);
    }
    
    public PushMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUSH_MSG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"IMG_ID\" INTEGER," + // 1: imgId
                "\"IMG_URL\" TEXT," + // 2: imgUrl
                "\"TITLE\" TEXT," + // 3: title
                "\"DATETIME\" TEXT);"); // 4: datetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUSH_MSG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PushMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer imgId = entity.getImgId();
        if (imgId != null) {
            stmt.bindLong(2, imgId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(3, imgUrl);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(5, datetime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PushMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer imgId = entity.getImgId();
        if (imgId != null) {
            stmt.bindLong(2, imgId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(3, imgUrl);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(5, datetime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PushMsg readEntity(Cursor cursor, int offset) {
        PushMsg entity = new PushMsg( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // imgId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // imgUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // datetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PushMsg entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setImgId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setImgUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDatetime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PushMsg entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PushMsg entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PushMsg entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}