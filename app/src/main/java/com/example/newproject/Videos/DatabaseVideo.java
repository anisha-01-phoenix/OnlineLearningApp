package com.example.newproject.Videos;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = VideoModel.class,version = 1)
public abstract class DatabaseVideo extends RoomDatabase {

    private static DatabaseVideo roomDatabase;
    public abstract VideoDao videoDao();
    public static synchronized DatabaseVideo getInstance(Context context)
    {
        if (roomDatabase==null)
        {
            roomDatabase= Room.databaseBuilder(context.getApplicationContext(),DatabaseVideo.class,"video_database").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return roomDatabase;
    }

    private static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(roomDatabase).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void>
    {
        private VideoDao videoDao;
        private PopulateDbAsync(DatabaseVideo db)
        {
            videoDao=db.videoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;

        }
    }
}

