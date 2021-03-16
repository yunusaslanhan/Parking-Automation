package model;

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
