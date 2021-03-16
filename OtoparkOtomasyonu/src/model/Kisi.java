package model;

import java.util.Date;

public class Kisi {

	private String isim;
	private String soyisim;
	private String plaka;
	private Date rezervasyonSaati;
	private String kalinacakSure;
	private boolean onay = false;

	public boolean isOnay() {
		return onay;
	}

	public void setOnay(boolean onay) {
		this.onay = onay;
	}

	public String getIsim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getSoyisim() {
		return soyisim;
	}

	public void setSoyisim(String soyisim) {
		this.soyisim = soyisim;
	}

	public String getPlaka() {
		return plaka;
	}

	public void setPlaka(String plaka) {
		this.plaka = plaka;
	}

	public Date getRezervasyonSaati() {
		return rezervasyonSaati;
	}

	public void setRezervasyonSaati(Date rezervasyonSaati) {
		this.rezervasyonSaati = rezervasyonSaati;
	}

	public String getKalinacakSure() {
		return kalinacakSure;
	}

	public void setKalinacakSure(String kalinacakSure) {
		this.kalinacakSure = kalinacakSure;
	}

	@Override
	public String toString() {
		return "Kisi [isim=" + isim + ", soyisim=" + soyisim + ", plaka=" + plaka + ", rezervasyonSaati="
				+ rezervasyonSaati + ", kalinacakSure=" + kalinacakSure + "]";
	}

}
