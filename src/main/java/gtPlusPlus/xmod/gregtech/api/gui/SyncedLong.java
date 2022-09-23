package gtPlusPlus.xmod.gregtech.api.gui;

import gtPlusPlus.xmod.gregtech.api.gui.SyncedValueManager.SendChanges;

/**
 * A variable of long type that is automatically synchronized between client and server correctly.
 *
 * @author glee8e
 */
class SyncedLong {
    private final int index;
    private long value;
    private final short[] pieces = new short[4];
    private int received;

    private boolean dirty = true;

    SyncedLong(int index) {
        this.index = index;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        if (this.value != value) {
            dirty = true;
            this.value = value;
        }
    }

    void detectAndSendChanges(SendChanges func, int timer) {
        if (dirty || (timer & 0xff) == 0) {
            for (int i = 0; i < 4; i++) {
                func.sendProgressBarUpdate(index + i, (int) ((value >> (16 * i)) & 0xffff));
            }
            dirty = false;
        }
    }

    private long getPiece(int index) {
        return ((long) pieces[index]) & 0xffff;
    }

    boolean updateProgressBar(int short1, int short2) {
        int offset = short1 - index;
        if (offset >= 0 && offset < 4) {
            pieces[offset] = (short) short2;
            received |= (1 << offset);
            if (received == 0b1111) {
                value = (getPiece(0)) | (getPiece(1) << 16) | (getPiece(2) << 32) | (getPiece(3) << 48);
                received = 0;
            }
            return true;
        }
        return false;
    }
}
