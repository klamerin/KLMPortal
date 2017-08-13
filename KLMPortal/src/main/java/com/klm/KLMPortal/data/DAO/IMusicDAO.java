package com.klm.KLMPortal.data.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import com.klm.KLMPortal.beans.MusicBean;

public interface IMusicDAO {
	
	public void addListeningSession(String band, String album, String portion, String comment, LocalDate date, Boolean concept);
	
	public void editListeningSession(String band, String album, String portion, String comment, LocalDate date, Boolean concept, Integer id);
	
	public ArrayList<MusicBean> getAllListeningSessions();
	
	public Collection<String> getAllBands();
	
	public Collection<String> getAllAlbums();
	
	public Collection<String> getAllBandsForAlbum(String album);
	
	public Collection<String> getAllAlbumsForBand(String band);
}
