package com.musicplayer.gdrivetv.sync;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J=\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u00052\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\b\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ=\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u00052\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\b\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lcom/musicplayer/gdrivetv/sync/GoogleDriveService;", "", "getFileMetadata", "Lcom/musicplayer/gdrivetv/sync/DriveFile;", "fileId", "", "fields", "apiKey", "authHeader", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "listFiles", "Lcom/musicplayer/gdrivetv/sync/DriveFilesResponse;", "query", "app_debug"})
public abstract interface GoogleDriveService {
    
    @retrofit2.http.GET(value = "drive/v3/files")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object listFiles(@retrofit2.http.Query(value = "q")
    @org.jetbrains.annotations.NotNull
    java.lang.String query, @retrofit2.http.Query(value = "fields")
    @org.jetbrains.annotations.NotNull
    java.lang.String fields, @retrofit2.http.Query(value = "key")
    @org.jetbrains.annotations.Nullable
    java.lang.String apiKey, @retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.Nullable
    java.lang.String authHeader, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.musicplayer.gdrivetv.sync.DriveFilesResponse> $completion);
    
    @retrofit2.http.GET(value = "drive/v3/files/{fileId}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getFileMetadata(@retrofit2.http.Path(value = "fileId")
    @org.jetbrains.annotations.NotNull
    java.lang.String fileId, @retrofit2.http.Query(value = "fields")
    @org.jetbrains.annotations.NotNull
    java.lang.String fields, @retrofit2.http.Query(value = "key")
    @org.jetbrains.annotations.Nullable
    java.lang.String apiKey, @retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.Nullable
    java.lang.String authHeader, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.musicplayer.gdrivetv.sync.DriveFile> $completion);
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}