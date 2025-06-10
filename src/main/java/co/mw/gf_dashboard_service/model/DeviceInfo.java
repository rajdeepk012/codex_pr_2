package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class DeviceInfo {

    @JsonProperty("app_version")
    private String appVersion;
    @JsonProperty("app_version_code")
    private int appVersionCode;
    @JsonProperty("battery_status")
    private String batteryStatus;
    @JsonProperty("app_battery_optimization_status")
    private boolean appBatteryOptimizationStatus;
    @JsonProperty("current_location")
    private Location currentLocation;
    @JsonProperty("location_permission")
    private String locationPermission;
    @JsonProperty("phone_model")
    private String phoneModel;
    @JsonProperty("os_version")
    private String osVersion;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("ram")
    private String ram;
    @JsonProperty("storage")
    private String storage;
    @JsonProperty("developer_mode_enabled")
    private boolean developerModeEnabled;
    @JsonProperty("calibration_status")
    private boolean calibrationStatus;
    @JsonProperty("screen_info")
    private ScreenInfo screenInfo;

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public boolean isAppBatteryOptimizationStatus() {
        return appBatteryOptimizationStatus;
    }

    public void setAppBatteryOptimizationStatus(boolean appBatteryOptimizationStatus) {
        this.appBatteryOptimizationStatus = appBatteryOptimizationStatus;
    }

    public String getLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(String locationPermission) {
        this.locationPermission = locationPermission;
    }

    public boolean isDeveloperModeEnabled() {
        return developerModeEnabled;
    }

    public void setDeveloperModeEnabled(boolean developerModeEnabled) {
        this.developerModeEnabled = developerModeEnabled;
    }

    public boolean isCalibrationStatus() {
        return calibrationStatus;
    }

    public void setCalibrationStatus(boolean calibrationStatus) {
        this.calibrationStatus = calibrationStatus;
    }

    public ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    public void setScreenInfo(ScreenInfo screenInfo) {
        this.screenInfo = screenInfo;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
