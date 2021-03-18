package com.redbeemedia.enigma.download;

import android.os.Parcel;
import android.os.Parcelable;

import com.redbeemedia.enigma.core.playable.IAssetPlayable;
import com.redbeemedia.enigma.core.playable.IPlayableHandler;

public class DownloadedPlayable implements IAssetPlayable {
    private IInternalDownloadData downloadData;

    public DownloadedPlayable(IInternalDownloadData downloadData) {
        this.downloadData = downloadData;
    }

    @Override
    public void useWith(IPlayableHandler playableHandler) {
        playableHandler.startUsingDownloadData(downloadData);
    }

    /*package-protected*/ IInternalDownloadData getDownloadData() {
        return downloadData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(downloadData, flags);
    }

    public static final Creator<DownloadedPlayable> CREATOR = new Creator<DownloadedPlayable>() {
        @Override
        public DownloadedPlayable createFromParcel(Parcel source) {
            Parcelable downloadData = source.readParcelable(getClass().getClassLoader());
            return new DownloadedPlayable((IInternalDownloadData) downloadData);
        }

        @Override
        public DownloadedPlayable[] newArray(int size) {
            return new DownloadedPlayable[size];
        }
    };

    @Override
    public String getAssetId() {
        return downloadData.getAssetId();
    }

    public Long getFileSize() {
        return downloadData.getFileSize();
    }

    /**
     * @return The stored DRM licence or null if the content is not DRM-protected.
     */
    public IDrmLicence getDrmLicence() {
        return downloadData.getDrmLicence();
    }

    /**
     * <h3>NOTE</h3>
     * <p>This interface is not part of the public API.</p>
     */
    public interface IInternalDownloadData extends Parcelable {
        String getAssetId();
        IDrmLicence getDrmLicence();
        Long getFileSize();
    }
}
