package com.hellobike.pmo.cockpit.enums;

public enum EnvironmentEnum {
    FAT("https://fat-star.hellobike.cn"),
    PRE("https://pre-star.hellobike.cn"),
    PRO("https://star.hellobike.cn");

    private final String url;

    EnvironmentEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
