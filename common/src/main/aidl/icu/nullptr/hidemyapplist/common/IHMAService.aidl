package icu.nullptr.hidemyapplist.common;

interface IHMAService {

    void stopService(boolean cleanEnv) = 0;

    void syncConfig(String json) = 1;

    int getServiceVersion() = 2;

    int getFilterCount() = 3;

    String getLogs() = 4;

    void clearLogs() = 5;

    void handlePackageEvent(String eventType, String packageName) = 6;

    String[] getPackageNames(int userId) = 7;

    PackageInfo getPackageInfo(String packageName, int userId) = 8;

    // CharSequence getAppLabel(in ApplicationInfo info) = 9;

    // Bitmap loadIcon(in ApplicationInfo info) = 10;
}
