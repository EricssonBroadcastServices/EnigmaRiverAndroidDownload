package com.redbeemedia.enigma.download;

import android.app.Application;

import com.redbeemedia.enigma.core.context.EnigmaRiverContext;
import com.redbeemedia.enigma.core.context.IModuleContextInitialization;
import com.redbeemedia.enigma.core.context.IModuleInfo;
import com.redbeemedia.enigma.core.context.IModuleInitializationSettings;
import com.redbeemedia.enigma.core.context.ModuleInfo;
import com.redbeemedia.enigma.core.context.exception.ModuleInitializationException;
import com.redbeemedia.enigma.core.format.EnigmaMediaFormat;
import com.redbeemedia.enigma.core.format.IMediaFormatSelector;
import com.redbeemedia.enigma.core.format.SimpleMediaFormatSelector;

public class EnigmaDownloadContext {
    private static final String NAME = EnigmaDownloadContext.class.getSimpleName();

    private static volatile InitializedContext initializedContext = null;

    private EnigmaDownloadContext() {} // Disable instantiation

    public static final IModuleInfo<Initialization> MODULE_INFO = new ModuleInfo<Initialization>(EnigmaDownloadContext.class) {
        @Override
        public Initialization createInitializationSettings() {
            return new Initialization();
        }
    };

    public static IMediaFormatSelector getDefaultDownloadFormatSelector() {
        assertInitialized();
        return initializedContext.defaultDownloadFormatSelector;
    }

    public static class Initialization implements IModuleInitializationSettings {
        private IMediaFormatSelector defaultDownloadFormatSelector = new SimpleMediaFormatSelector(
                EnigmaMediaFormat.DASH().unenc(),
                EnigmaMediaFormat.DASH().widevine(),
                EnigmaMediaFormat.MP3().unenc()
        );


        public IMediaFormatSelector getDefaultDownloadFormatSelector() {
            return defaultDownloadFormatSelector;
        }

        public Initialization setDefaultDownloadFormatSelector(IMediaFormatSelector defaultDownloadFormatSelector) {
            this.defaultDownloadFormatSelector = defaultDownloadFormatSelector;
            return this;
        }
    }

    /**
     * Called by core module through reflection.
     */
    private static synchronized void initialize(IModuleContextInitialization initialization) throws ModuleInitializationException {
        if(initializedContext == null) {
            initializedContext = new InitializedContext(initialization);
        } else {
            throw new IllegalStateException(NAME+" already initialized.");
        }
    }

    public static IMetadataManager getMetadataManager() {
        assertInitialized();
        return initializedContext.metadataManager;
    }

    public static synchronized void assertInitialized() {
        if(initializedContext == null) {
            // If EnigmaRiverContext is not yet initialized,
            // getVersion() will throw an exception. This
            // indicates that the reason this module is not
            // yet initialized is that the parent module is
            // not initialized.
            String version = EnigmaRiverContext.getVersion();
            throw new IllegalStateException(NAME+" was not initialized from core module. Make sure "+version+" is used for all Enigma River SDK modules.");
        }
    }

    private static class InitializedContext {
        private final IMetadataManager metadataManager;
        private final IMediaFormatSelector defaultDownloadFormatSelector;

        public InitializedContext(IModuleContextInitialization initialization) {
            Application application = initialization.getApplication();

            metadataManager = new DefaultMetadataManager(application);

            Initialization moduleSettings = initialization.getModuleSettings(MODULE_INFO);
            defaultDownloadFormatSelector = moduleSettings.defaultDownloadFormatSelector;
        }
    }
}
