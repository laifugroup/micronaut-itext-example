package org.example;

public class Item {
    private String title="金蟾寻宝 扫码探索";
    private String url="https://prize.jxyuan.cn/#/pages/prize-detail/index?id=";
    private String code="";
    private String slogan="做最好的户外探索游戏";

    public Item(String code) {
        this.code = code;
    }

    public Item(String url, String code) {
        this.url = url;
        this.code = code;
    }
    public Item(String title, String url, String code, String slogan) {
        this.title = title;
        this.url = url;
        this.code = code;
        this.slogan = slogan;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public String getSlogan() {
        return slogan;
    }
}
