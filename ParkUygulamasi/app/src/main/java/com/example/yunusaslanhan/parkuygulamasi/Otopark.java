package com.example.yunusaslanhan.parkuygulamasi;

import java.util.ArrayList;
import java.util.List;

public class Otopark {

    private String isim;
    private int toplamYerSayisi;
    private int bosYerSayisi;
    private String longitude;
    private String latitude;
    private List<Kisi> rezerveEdenler = new ArrayList<>();;
    private String kullaniciAdi;
    private String sifre;


    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public int getBosYerSayisi() {
        return bosYerSayisi;
    }

    public void setBosYerSayisi(int bosYerSayisi) {
        this.bosYerSayisi = bosYerSayisi;
    }

    public int getToplamYerSayisi() {
        return toplamYerSayisi;
    }

    public void setToplamYerSayisi(int toplamYerSayisi) {
        this.toplamYerSayisi = toplamYerSayisi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public void rezerveEkle(Kisi kisi) {
        rezerveEdenler.add(kisi);
    }

    public void rezerveIptal(Kisi kisi) {
        rezerveEdenler.remove(kisi);
    }

    public static List<Otopark> rastgeleOtoparkList() {

        List<Otopark> otoparkListesi = new ArrayList<>();
        Otopark o1 = new Otopark();
        o1.setKullaniciAdi("otopark1");
        o1.setSifre("123");
        o1.setIsim("Otopark 1");
        o1.setToplamYerSayisi(25);
        o1.setBosYerSayisi(0);
        o1.setLatitude("40.876675");
        o1.setLongitude("31.215072");

        Otopark o2 = new Otopark();
        o1.setKullaniciAdi("otopark2");
        o1.setSifre("123");
        o2.setIsim("Otopark 2");
        o2.setToplamYerSayisi(13);
        o2.setBosYerSayisi(0);
        o2.setLatitude("40.843207");
        o2.setLongitude("31.153150");

        Otopark o3 = new Otopark();
        o1.setKullaniciAdi("otopark3");
        o1.setSifre("123");
        o3.setIsim("Otopark 3");
        o3.setToplamYerSayisi(77);
        o3.setBosYerSayisi(0);
        o3.setLatitude("40.850051");
        o3.setLongitude("31.182907");

        Otopark o4 = new Otopark();
        o1.setKullaniciAdi("otopark4");
        o1.setSifre("123");
        o4.setIsim("Otopark 4");
        o4.setToplamYerSayisi(9);
        o4.setBosYerSayisi(0);//1 dk tamam
        o4.setLatitude("40.872130");
        o4.setLongitude("31.171446");

        otoparkListesi.add(o1);
        otoparkListesi.add(o2);
        otoparkListesi.add(o3);
        otoparkListesi.add(o4);

        return otoparkListesi;

    }

    public List<Kisi> getRezerveEdenler() {
        return rezerveEdenler;
    }

    public void setRezerveEdenler(List<Kisi> rezerveEdenler) {
        this.rezerveEdenler = rezerveEdenler;
    }

    @Override
    public String toString() {
        return "Otopark [isim=" + isim + ", toplamYerSayisi=" + toplamYerSayisi + ", bosYerSayisi=" + bosYerSayisi
                + ", longitude=" + longitude + ", latitude=" + latitude + ", rezerveEdenler=" + rezerveEdenler
                + ", kullaniciAdi=" + kullaniciAdi + ", sifre=" + sifre + "]";
    }




}
