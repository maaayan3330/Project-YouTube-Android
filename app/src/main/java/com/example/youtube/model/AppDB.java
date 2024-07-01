package com.example.youtube.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.model.converters.UriConverter;
import com.example.youtube.model.converters.CommentConverter;

@Database(entities = {Video.class, User.class}, version = 2)
@TypeConverters({CommentConverter.class, UriConverter.class})
public abstract class AppDB extends RoomDatabase{
    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
}
