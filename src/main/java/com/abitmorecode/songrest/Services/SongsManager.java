package com.abitmorecode.songrest.Services;

import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.SongControllerException.NoIdAvailableException;
import com.abitmorecode.songrest.SongControllerException.SameSongAlreadyExistException;
import com.abitmorecode.songrest.SongControllerException.SongDoesntExistException;
import com.abitmorecode.songrest.SongControllerException.SongIdAlreadyExistException;

import java.util.List;

public interface SongsManager {

	/**
	 * getter for a specific song in list
	 *
	 * @param id id of Song
	 *
	 * @return Song searched for
	 *
	 * @throws SongDoesntExistException thrown, if id isn't used by a Song
	 */
	Song getSpecificSong(int id) throws SongDoesntExistException;

	/**
	 * getter for all Songs
	 *
	 * @return List of all songs
	 */
	List<Song> getAllSongs();

	/**
	 * adds a Song Object
	 *
	 * @param song Song
	 *
	 * @throws NoIdAvailableException thrown, if there is no unused positive integer id available
	 */
	void addSong(Song song) throws NoIdAvailableException;

	/**
	 * deletes a Song over id
	 *
	 * @param id id int
	 *
	 * @throws SongDoesntExistException thrown, if Song with id doesn't exist
	 */
	void deleteSong(int id) throws SongDoesntExistException;

	/**
	 * reset method for deleting all requests-able Songs
	 */
	void reset();
}
