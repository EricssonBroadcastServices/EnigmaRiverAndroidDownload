package com.redbeemedia.enigma.download.assetdownload;

import com.redbeemedia.enigma.core.util.IStateMachine;
import com.redbeemedia.enigma.core.util.IStateMachineBuilder;
import com.redbeemedia.enigma.core.util.StateMachineBuilder;

/**
 * <h3>NOTE</h3>
 * <p>This class is not part of the public API.</p>
 */
public class AssetDownloadStateMachineFactory {
    public static IStateMachine<AssetDownloadState> create(AssetDownloadState initialState) {
        IStateMachineBuilder<AssetDownloadState> builder = new StateMachineBuilder<>();

        builder.setInvalidStateTransitionHandler(StateMachineBuilder.InvalidTransitionHandler.STRICT_LOGGING("AssetDownloadState"));

        for(AssetDownloadState state : AssetDownloadState.values()) {
            builder.addState(state);
        }

        builder.setInitialState(initialState);

        builder.addDirectTransition(AssetDownloadState.IN_PROGRESS, AssetDownloadState.PAUSED);
        builder.addDirectTransition(AssetDownloadState.IN_PROGRESS, AssetDownloadState.DONE);
        builder.addDirectTransition(AssetDownloadState.IN_PROGRESS, AssetDownloadState.FAILED);
        builder.addDirectTransition(AssetDownloadState.IN_PROGRESS, AssetDownloadState.CANCELLED);

        builder.addDirectTransition(AssetDownloadState.PAUSED, AssetDownloadState.IN_PROGRESS);
        builder.addDirectTransition(AssetDownloadState.PAUSED, AssetDownloadState.FAILED);
        builder.addDirectTransition(AssetDownloadState.PAUSED, AssetDownloadState.CANCELLED);

        builder.addDirectTransition(AssetDownloadState.FAILED, AssetDownloadState.CANCELLED);

        return builder.build();
    }
}
