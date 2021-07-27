package com.example.newproject.Videos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.newproject.VideoModel;

import java.util.List;

public class Repository {

    private VideoDao videoDao;
    private LiveData<List<VideoModel>> allVideos;
    public  Repository(Application application)
    {
        DatabaseVideo databaseVideo=DatabaseVideo.getInstance(application);
        videoDao=databaseVideo.videoDao();
        allVideos=videoDao.getAllVideos();
    }

    public void insert(VideoModel videoModel)
    {
        new InsertVideoAsyncTask(videoDao).execute(videoModel);
    }

    public void update(VideoModel videoModel)
    {
        new UpdateVideoAsyncTask(videoDao).execute(videoModel);
    }

    public void delete(VideoModel videoModel)
    {
        new DeleteVideoAsyncTask(videoDao).execute(videoModel);
    }

    public LiveData<List<VideoModel>> getAllVideos() {
        return allVideos;
    }

    private static class InsertVideoAsyncTask extends AsyncTask<VideoModel,Void,Void>
    {
        private VideoDao videoDao;
        public InsertVideoAsyncTask(VideoDao videoDao) {

            this.videoDao=videoDao;
        }

        @Override
        protected Void doInBackground(VideoModel... videoModels) {
            videoDao.insert(videoModels[0]);
            return null;
        }
    }

    private static class UpdateVideoAsyncTask extends AsyncTask<VideoModel,Void,Void>
    {
        private VideoDao videoDao;
        public UpdateVideoAsyncTask(VideoDao videoDao) {

            this.videoDao=videoDao;
        }

        @Override
        protected Void doInBackground(VideoModel... videoModels) {
            videoDao.update(videoModels[0]);
            return null;
        }
    }

    private static class DeleteVideoAsyncTask extends AsyncTask<VideoModel,Void,Void>
    {
        private VideoDao videoDao;
        public DeleteVideoAsyncTask(VideoDao videoDao) {

            this.videoDao=videoDao;
        }

        @Override
        protected Void doInBackground(VideoModel... videoModels) {
            videoDao.delete(videoModels[0]);
            return null;
        }
    }
}
