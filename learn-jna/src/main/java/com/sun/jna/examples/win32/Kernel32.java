package com.sun.jna.examples.win32;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

// kernel32.dll uses the __stdcall calling convention (check the function
// declaration for "WINAPI" or "PASCAL"), so extend StdCallLibrary
// Most C libraries will just extend com.sun.jna.Library,
public interface Kernel32 extends StdCallLibrary {
    // Method declarations, constant and structure definitions go here
    Kernel32 INSTANCE = (Kernel32)
            Native.loadLibrary("kernel32", Kernel32.class);
    // Optional: wraps every call to the native library in a
// synchronized block, limiting native calls to one at a time
    Kernel32 SYNC_INSTANCE = (Kernel32)
            Native.synchronizedLibrary(INSTANCE);

    public static class SYSTEMTIME extends Structure {
        public short wYear;
        public short wMonth;
        public short wDayOfWeek;
        public short wDay;
        public short wHour;
        public short wMinute;
        public short wSecond;
        public short wMilliseconds;

        protected List getFieldOrder() {
            return Arrays.asList(new String[]{
                    "wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds",
            });
        }
    }

    void GetSystemTime(SYSTEMTIME result);
}