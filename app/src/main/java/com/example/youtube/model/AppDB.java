package com.example.youtube.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.model.converters.DateConverter;
import com.example.youtube.model.converters.UriConverter;
import com.example.youtube.model.converters.CommentConverter;
import com.example.youtube.model.daos.UserDao;
import com.example.youtube.model.daos.VideoDao;

@Database(entities = {Video.class, User.class}, version = 3)
@TypeConverters({CommentConverter.class, UriConverter.class, DateConverter.class})
public abstract class AppDB extends RoomDatabase{
    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
}
