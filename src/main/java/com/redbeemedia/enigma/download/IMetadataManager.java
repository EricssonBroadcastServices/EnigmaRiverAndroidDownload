package com.redbeemedia.enigma.download;

//Note: If this is made public, add a bess class and tell implementors to extend that instead, to
//      ensure backwards comatibility.

/**
 * <h3>NOTE</h3>
 * <p>This interface is not part of the public API.</p>
 */
public interface IMetadataManager {
    void store(String contentId, byte[] data);
    void clear(String contentId);
    byte[] load(String contentId);
}
