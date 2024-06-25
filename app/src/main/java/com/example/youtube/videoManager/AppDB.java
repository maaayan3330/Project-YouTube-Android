package com.example.youtube.videoManager;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
@Database(entities = {Video.class}, version = 1)
@TypeConverters({CommentConverter.class})
public abstract class AppDB extends RoomDatabase{
    public abstract VideoDao videoDao();
}
